package com.FoodServe.Dilevery.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.FoodServe.Dilevery.Userrepository.OrderRepository;
import com.FoodServe.Dilevery.UtilityClass.HmacSHA256;
import com.FoodServe.Dilevery.dto.PaymentVerifyRequestDto;
import com.FoodServe.Dilevery.entity.OrdersEntity;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@Service
public class PaymentService {

   
	@Value("${razorpay.key}")
	private String secret;
	
	

	private final OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Map<String, Object> createPaymentOrder(Long orderId) throws Exception {

        OrdersEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        RazorpayClient client = new RazorpayClient("rzp_test_Snk8buh7h4y2Uq", secret);

        JSONObject options = new JSONObject();
        options.put("amount", order.getTotalAmount() * 100);
        options.put("currency", "INR");
        options.put("receipt", "order_rcptid_" + orderId);

        Order razorpayOrder = client.orders.create(options);

        Map<String, Object> response = new HashMap<>();
        response.put("razorpayOrderId", razorpayOrder.get("id"));
        response.put("amount", order.getTotalAmount());
        response.put("orderId", orderId);

        return response;
    }
    
    
    public boolean verifyPayment(
            PaymentVerifyRequestDto request) {

        try {

            String generatedSignature =
                    HmacSHA256.calculateHMAC(
                            request.getRazorpayOrderId()
                                    + "|" +
                            request.getRazorpayPaymentId(),
                            secret
                    );

            return generatedSignature.equals(
                    request.getRazorpaySignature());

        } catch (Exception e) {
            return false;
        }
    }
   
}