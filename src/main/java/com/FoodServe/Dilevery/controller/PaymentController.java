package com.FoodServe.Dilevery.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FoodServe.Dilevery.UtilityClass.HmacSHA256;
import com.FoodServe.Dilevery.dto.PaymentVerifyRequestDto;
import com.FoodServe.Dilevery.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

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

            String generatedSignature =
                    HmacSHA256.calculateHMAC(
                            request.getRazorpayOrderId() + "|" +
                            request.getRazorpayPaymentId(),
                            "rzp_test_SjzVLofk9UEl3y"
                    );

            if(generatedSignature.equals(request.getRazorpaySignature())) {

                return ResponseEntity.ok(
                        "Payment Success & Order Confirmed"
                );
            }

            return ResponseEntity.badRequest()
                    .body("Invalid Signature");

        } catch (Exception e) {

            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }
}