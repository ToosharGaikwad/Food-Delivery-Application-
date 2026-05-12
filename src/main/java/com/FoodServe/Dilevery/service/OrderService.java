package com.FoodServe.Dilevery.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

        order.setOrderStatus(OrderStatus.PLACED);

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
    
    
}