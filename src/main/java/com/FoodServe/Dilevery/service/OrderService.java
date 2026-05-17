package com.FoodServe.Dilevery.service;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Documented;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import com.FoodServe.Dilevery.entity.OrderItem;
import com.FoodServe.Dilevery.entity.OrdersEntity;
import com.FoodServe.Dilevery.entity.Product;
import com.FoodServe.Dilevery.entity.User;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
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
    

    public OrdersEntity confirmOrder(Long orderId) {

        OrdersEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // ✅ Only confirm if still pending
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order already processed");
        }

        order.setOrderStatus(OrderStatus.CONFIRMED);

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
	
	
	@Transactional
	public ResponseEntity<byte[]> downloadReceipt(Long orderId) {

	    try {

	        OrdersEntity order = orderRepository.findById(orderId)
	                .orElseThrow(() -> new RuntimeException("Order not found"));

	        if(order.getItems() == null) {
	            throw new RuntimeException("Items not found");
	        }

	        ByteArrayOutputStream out = new ByteArrayOutputStream();

	        Document document = new Document();

	        PdfWriter.getInstance(document, out);

	        document.open();

	        document.add(new Paragraph("Food Delivery Receipt"));
	        document.add(new Paragraph("------------------------"));

	        document.add(new Paragraph("Order ID : " + order.getOrderId()));

	        document.add(new Paragraph(""));

	        for (OrderItem item : order.getItems()) {

	            document.add(
	                new Paragraph(
	                    item.getProduct().getName()
	                    + " X "
	                    + item.getQuantity()
	                    + " = ₹"
	                    + (item.getPrice() * item.getQuantity())
	                )
	            );
	        }

	        document.add(new Paragraph(""));
	        document.add(new Paragraph("Payment Status : SUCCESS"));

	        document.close();

	        HttpHeaders headers = new HttpHeaders();

	        headers.add(
	            "Content-Disposition",
	            "attachment; filename=receipt.pdf"
	        );

	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(out.toByteArray());

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity.internalServerError().build();
	    }
	}
	public OrdersEntity getLatestOrder(String email) {

	    return orderRepository
	            .findTopByUserEmailOrderByOrderDateDesc(email)
	            .orElseThrow(() ->
	                new RuntimeException("No order found"));
	}
	
	}

	 

	

	
    
    
