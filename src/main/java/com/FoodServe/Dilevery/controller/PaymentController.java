package com.FoodServe.Dilevery.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FoodServe.Dilevery.UtilityClass.HmacSHA256;
import com.FoodServe.Dilevery.dto.PaymentVerifyRequestDto;
import com.FoodServe.Dilevery.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    
//    @Value("{${razorpay.key}")
//    private String secret;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create/{orderId}")
    public ResponseEntity<?> createPayment(
            @PathVariable("orderId") Long id) throws Exception {

        return ResponseEntity.ok(
                paymentService.createPaymentOrder(id));
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestBody PaymentVerifyRequestDto request) {

        try {

            boolean isValid =
                    paymentService.confirmPayment(request);

            if(isValid) {

                return ResponseEntity.ok(
                        Map.of(
                            "message",
                            "Payment Success & Order Confirmed"
                        )
                );
            }

            return ResponseEntity.badRequest()
                    .body(
                        Map.of(
                            "message",
                            "Invalid Signature"
                        )
                    );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body(
                        Map.of(
                            "message",
                            e.getMessage()
                        )
                    );
        }
    }
}