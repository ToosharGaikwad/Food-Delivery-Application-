package com.FoodServe.Dilevery.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.FoodServe.Dilevery.dto.OrderRequestDTO;
import com.FoodServe.Dilevery.entity.OrdersEntity;
import com.FoodServe.Dilevery.service.OrderService;
@CrossOrigin(origins = "http://localhost:4200")
@RestController 
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderservice) {
        this.orderService = orderservice;
    }

    // 👤 USER + ADMIN → Place order

    @PostMapping
    public OrdersEntity createOrder(@RequestBody OrderRequestDTO order){
    	System.out.println("creaTE order called");
        return orderService.createOrder(order);
    }
    

    
    
    
    @PostMapping("/fail/{orderId}")
    public OrdersEntity failOrder(@PathVariable("orderId") Long orderId){
        return orderService.failOrder(orderId);
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

    @PutMapping("/{id}/status")   // ✅ FIXED
    public OrdersEntity updateStatus(@PathVariable("id") Long id,
                                    @RequestBody OrderRequestDTO dto) {

        return orderService.updateStatus(id, dto.getStatus());
    }
    
}