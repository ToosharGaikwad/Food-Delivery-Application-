package com.FoodServe.Dilevery.dto;


import java.time.LocalDateTime;
import java.util.List;

import com.FoodServe.Dilevery.Enum.OrderStatus;

public class OrderResponseDTO {

    private Long orderId;
    private OrderStatus orderStatus;
    private double totalAmount;
    private LocalDateTime orderDate;
    private List<OrderItemResponseDTO> items;

    // getters & setters
    
    
}