package com.FoodServe.Dilevery.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.FoodServe.Dilevery.dto.OrderRequestDTO;
import com.FoodServe.Dilevery.entity.OrdersEntity;
import com.FoodServe.Dilevery.service.OrderService;

@RestController 
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderservice) {
        this.orderService = orderservice;
    }

    // 👤 USER + ADMIN → Place order
    @PostMapping
    @PreAuthorize("hasRole('USER','ADMIN)")
    public OrdersEntity placeOrder(@RequestBody OrderRequestDTO order){
        return orderService.placeOrder(order);
    }

    // 👤 USER + MANAGER + ADMIN → View single order
    
    @GetMapping("/{id}")
    public OrdersEntity getorder(@PathVariable("id") Long id) {
        return orderService.getorder(id);
    }

    // 👨‍💼 ADMIN ONLY → View all orders
   
    @GetMapping
    public List<OrdersEntity> Allorders(){
        return orderService.Allorders();
    }

    // 🏪 MANAGER + ADMIN → Update order status
//    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_MANAGER')")
//    @PutMapping("/{id}/status")
//    public OrdersEntity updateStatus(@PathVariable("id") Long id,
//                                     @RequestBody String status) {
//        return orderService.updateStatus(id, status);
//    }
    
}