package com.FoodServe.Dilevery.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.FoodServe.Dilevery.Enum.OrderStatus;
import com.FoodServe.Dilevery.Userrepository.OrderRepository;
import com.FoodServe.Dilevery.Userrepository.ProductRepository;
import com.FoodServe.Dilevery.Userrepository.UserRepository;
import com.FoodServe.Dilevery.dto.OrderItemRequestDTO;
import com.FoodServe.Dilevery.dto.OrderRequestDTO;
import com.FoodServe.Dilevery.entity.DeliveryBoyEntity;
import com.FoodServe.Dilevery.entity.OrderItem;
import com.FoodServe.Dilevery.entity.OrdersEntity;
import com.FoodServe.Dilevery.entity.Product;
import com.FoodServe.Dilevery.entity.User;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.log.SysoCounter;
import com.itextpdf.text.pdf.PdfWriter;
import com.FoodServe.Dilevery.Userrepository.DeliveryBoyRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final DeliveryBoyService deliveryBoyService;
    private final DeliveryBoyRepository deliveryBoyRepository;
    
    public OrderService(OrderRepository orderRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            DeliveryBoyService deliveryBoyService,
            DeliveryBoyRepository deliveryBoyRepository
           ) {

this.orderRepository = orderRepository;
this.productRepository = productRepository;
this.userRepository = userRepository;
this.deliveryBoyService = deliveryBoyService;
this.deliveryBoyRepository = deliveryBoyRepository;

}

    public OrdersEntity createOrder(OrderRequestDTO dto) {

        OrdersEntity order = new OrdersEntity();
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentMode(dto.getPaymentMode());

        // ✅ important
        order.setOrderStatus(OrderStatus.PENDING);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);

        double total = 0;
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemRequestDTO itemDto : dto.getItems()) {

            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(product.getPrice());
            item.setOrder(order);
            System.out.println(order +"order set");
            total += product.getPrice() * itemDto.getQuantity();
            items.add(item);
        }

        order.setItems(items);
        order.setTotalAmount(total);
        System.out.println(items +" all items");
        return orderRepository.save(order);
    }
  
    
       
    @Transactional
    public OrdersEntity confirmOrder(Long orderId) {

        OrdersEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order already processed");
        }

        // confirm order
        order.setOrderStatus(OrderStatus.CONFIRMED);

        // get available boys
        List<DeliveryBoyEntity> boys =
                deliveryBoyRepository.findByAvailableTrue();

        if (boys.isEmpty()) {
            throw new RuntimeException("No delivery boy available");
        }

        // random boy selection
        Random random = new Random();

        DeliveryBoyEntity selectedBoy =
                boys.get(random.nextInt(boys.size()));

        // call existing method
        deliveryBoyService.assignDeliveryBoy(orderId, selectedBoy.getId());

        return orderRepository.save(order);
    }
    
    
    public OrdersEntity getorder(Long orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("order not found"));
    }

	public List<OrdersEntity> Allorders() {
		
		return orderRepository.findAll();
	}

	public OrdersEntity updateStatus(Long id, String status) {
		 OrdersEntity order = orderRepository.findById(id)
		            .orElseThrow(() -> new RuntimeException("Order not found"));

		    // Convert String → Enum
		    OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());

		    order.setOrderStatus(newStatus);  // ✅ update entity

		return orderRepository.save(order);
	}

	public OrdersEntity failOrder(Long orderId) {

	    OrdersEntity order = orderRepository.findById(orderId)
	            .orElseThrow(() -> new RuntimeException("Order not found"));

	    // Optional safety check
	    if (order.getOrderStatus() != OrderStatus.PENDING) {
	        throw new RuntimeException("Order already processed");
	    }

	    order.setOrderStatus(OrderStatus.CANCELLED);

	    return orderRepository.save(order);
	}
	public OrdersEntity getLatestOrder(String email) {
	    return orderRepository
	            .findTopByUserEmailOrderByOrderDateDesc(email)
	            .orElseThrow(() -> new RuntimeException("No order found for: " + email));
	}
	
	@Transactional(readOnly = true)
	public ResponseEntity<byte[]> downloadReceipt(Long orderId) {

	    try {
	        System.out.println("Download receipt called for: " + orderId);

	        OrdersEntity order = orderRepository.findByIdWithItems(orderId)
	                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

	        List<OrderItem> items = order.getItems();

	        if (items == null || items.isEmpty()) {
	            throw new RuntimeException("No items found for order: " + orderId);
	        }

	        System.out.println("Items count: " + items.size());

	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        Document document = new Document(PageSize.A6);
	        PdfWriter.getInstance(document, out);
	        document.open();

	        document.add(new Paragraph("Food Delivery Receipt"));
	        document.add(new Paragraph("----------------------"));
	        document.add(new Paragraph("Order ID : " + order.getOrderId()));
	        document.add(new Paragraph(" "));

	        double grandTotal = 0;

	        for (OrderItem item : items) {
	            if (item == null || item.getProduct() == null) {
	                System.out.println("Skipping null item");
	                continue;
	            }

	            String name = item.getProduct().getName();
	            double qty = item.getQuantity();
	            double price = item.getPrice();
	            double lineTotal = qty * price;
	            grandTotal += lineTotal;

	            document.add(new Paragraph(name + " x " + (int) qty + " = Rs." + lineTotal));
	        }

	        document.add(new Paragraph(" "));
	        document.add(new Paragraph("Total Amount : Rs." + grandTotal));
	        document.add(new Paragraph("Payment Mode : " + order.getPaymentMode()));
	        document.add(new Paragraph("Order Status : " + order.getOrderStatus()));

	        document.close();

	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition",
	                "attachment; filename=receipt-" + orderId + ".pdf");

	        System.out.println("Receipt generated successfully for: " + orderId);

	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(out.toByteArray());

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException(e);
	    }
	}
	
	}

	 

	

	
    
    
