package com.FoodServe.Dilevery.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.FoodServe.Dilevery.Enum.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "payments")
public class paymentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Your internal order ID (optional but useful)
	private String receipt;
	
	// razorpay fileds
	 @Column(name = "razorpay_order_id")
	private String razorpayOrderId;
	 
	 @Column(name = "razorpay_payment_id")
	private String  razorpayPaymentId;
	 
	  @Column(name = "razorpay_signature")
	private String razorpaySignature;
	
	  // payments detail fileds
	  
	  private Integer amount;   // in paise
	    private String currency;

	    @Enumerated(EnumType.STRING)
	    private PaymentStatus status;

	  
	  
	  
	//payment fileds
	
	    @CreationTimestamp
	    private LocalDateTime createdAt;

	    @UpdateTimestamp
	    private LocalDateTime updatedAt;
	   
	    
	
	 public Long getId() { return id; }

	    public String getReceipt() { return receipt; }
	    public void setReceipt(String receipt) { this.receipt = receipt; }

	    public String getRazorpayOrderId() { return razorpayOrderId; }
	    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }

	    public String getRazorpayPaymentId() { return razorpayPaymentId; }
	    public void setRazorpayPaymentId(String razorpayPaymentId) { this.razorpayPaymentId = razorpayPaymentId; }

	    public String getRazorpaySignature() { return razorpaySignature; }
	    public void setRazorpaySignature(String razorpaySignature) { this.razorpaySignature = razorpaySignature; }

	    public Integer getAmount() { return amount; }
	    public void setAmount(Integer amount) { this.amount = amount; }

	    public String getCurrency() { return currency; }
	    public void setCurrency(String currency) { this.currency = currency; }

	    public PaymentStatus getStatus() { return status; }
	    public void setStatus(PaymentStatus status) { this.status = status; }

	    public LocalDateTime getCreatedAt() { return createdAt; }

	    public LocalDateTime getUpdatedAt() { return updatedAt; }
	    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
	
	
}
