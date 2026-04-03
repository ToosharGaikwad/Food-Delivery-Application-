package com.FoodServe.Dilevery.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.FoodServe.Dilevery.entity.DeliveryBoyEntity;
import com.FoodServe.Dilevery.entity.OrdersEntity;
import com.FoodServe.Dilevery.service.DeliveryBoyService;

@RequestMapping("/Del")
@RestController
public class DeliveryBoyController {
	private final DeliveryBoyService deliveryBoyService;
	
	public DeliveryBoyController(DeliveryBoyService deliveryBoyService) {
		this.deliveryBoyService =deliveryBoyService ;
	}
	
	// ✅ Assign delivery boy to order
    @PatchMapping("/assign")
    public OrdersEntity assignDeliveryBoy(
            @RequestParam Long orderId,
            @RequestParam Long boyId) {

        return deliveryBoyService.assignDeliveryBoy(orderId, boyId);
    }

    // ✅ Mark order as delivered
  
    @PreAuthorize("hasAnyRole('USER','ADMIN','DELIVERY_BOY')")
    @PatchMapping("/order/{orderId}/delivered")
    public OrdersEntity markOrderDelivered(@PathVariable Long orderId) {
        return deliveryBoyService.markDelivered(orderId);
    }
    // ✅ ADD DELIVERY BOY (FIRST API)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addBoy")
    public DeliveryBoyEntity addDeliveryBoy(@RequestBody DeliveryBoyEntity boy) {
        return deliveryBoyService.addDeliveryBoy(boy);
    }
    
    
}
