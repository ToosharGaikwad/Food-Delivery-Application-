package com.FoodServe.Dilevery.service;

import org.springframework.stereotype.Service;

import com.FoodServe.Dilevery.Enum.OrderStatus;
import com.FoodServe.Dilevery.Userrepository.DeliveryBoyRepository;
import com.FoodServe.Dilevery.Userrepository.OrderRepository;
import com.FoodServe.Dilevery.entity.DeliveryBoyEntity;
import com.FoodServe.Dilevery.entity.OrdersEntity;

@Service
public class DeliveryBoyService {

    private final DeliveryBoyRepository deliveryBoyRepository;
    private final OrderRepository orderRepository;

    public DeliveryBoyService(DeliveryBoyRepository deliveryBoyRepository,
                              OrderRepository orderRepository) {
        this.deliveryBoyRepository = deliveryBoyRepository;
        this.orderRepository = orderRepository;
    }

    public OrdersEntity assignDeliveryBoy(Long orderId, Long boyId) {
        OrdersEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        DeliveryBoyEntity boy = deliveryBoyRepository.findById(boyId)
                .orElseThrow(() -> new RuntimeException("Delivery boy not found"));

        if (!boy.isAvailable()) {
            throw new RuntimeException("Delivery boy not available");
        }

        order.setDeliveryBoy(boy);
        order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);

        boy.setAvailable(false);

        return orderRepository.save(order);
    }

    public OrdersEntity markDelivered(Long orderId) {

        OrdersEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        DeliveryBoyEntity boy = order.getDeliveryBoy();

        order.setOrderStatus(OrderStatus.DELIVERED);
        boy.setAvailable(true);

        return orderRepository.save(order);
    }
    
 // ✅ ADD DELIVERY BOY (FIRST STEP)
    public DeliveryBoyEntity addDeliveryBoy(DeliveryBoyEntity boy) {

        // when a delivery boy is created, he should be available
        boy.setAvailable(true);

        return deliveryBoyRepository.save(boy);
    }

	

	
}