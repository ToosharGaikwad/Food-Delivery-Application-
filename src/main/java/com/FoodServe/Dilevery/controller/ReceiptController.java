package com.FoodServe.Dilevery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.FoodServe.Dilevery.entity.OrdersEntity;
import com.FoodServe.Dilevery.service.OrderService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ReceiptController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/receipt/{orderId}")
    public ResponseEntity<byte[]> downloadReceipt(
    		@PathVariable("orderId") Long orderId) {

        System.out.println("Receipt Controller Hit");

        return orderService.downloadReceipt(orderId);
    }
    
    @GetMapping("/orders/latest")
    public OrdersEntity latestOrder(){

        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();
        System.out.println(auth.getName());
        return orderService.getLatestOrder(email);
    }
}