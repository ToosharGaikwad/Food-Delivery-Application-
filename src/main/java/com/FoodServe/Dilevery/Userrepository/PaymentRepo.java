package com.FoodServe.Dilevery.Userrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FoodServe.Dilevery.entity.paymentEntity;

public interface PaymentRepo extends JpaRepository<paymentEntity, Long> {

	paymentEntity findByRazorpayOrderId(String orderId);

	paymentEntity findByRazorpayPaymentId(String paymentId);
}
