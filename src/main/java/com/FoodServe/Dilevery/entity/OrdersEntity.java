package com.FoodServe.Dilevery.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.FoodServe.Dilevery.Enum.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class OrdersEntity {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "orderId")
	    private Long orderId;
	    
	    @Column(name = "order_date")
	    private LocalDateTime orderDate;
	    
	    @Enumerated(EnumType.STRING)
	    @Column(name = "order_status")
	    private OrderStatus orderStatus;
	    
	    @Column(name = "totalAmount")
	    private double totalAmount;
	    
	    @Column(name = "payment_mode")
	    private String paymentMode;
	    
	    

		@Column(name = "payment_status")
	    private String paymentStatus;
	    
		@ManyToOne
		@JoinColumn(name = "delivery_boy_id")
		private DeliveryBoyEntity deliveryBoy;
		
		
	    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	    private List<OrderItem> items;
	    
	    // Many orders belong to one user
	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User user;
	    
	    
		public DeliveryBoyEntity getDeliveryBoy() {
			return deliveryBoy;
		}


		public void setDeliveryBoy(DeliveryBoyEntity deliveryBoy) {
			this.deliveryBoy = deliveryBoy;
		}


		public Long getOrderId() {
			return orderId;
		}
		

	    public List<OrderItem> getItems() {
			return items;
		}

		public void setItems(List<OrderItem> items) {
			this.items = items;
		}
		
		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}

		public LocalDateTime getOrderDate() {
			return orderDate;
		}

		public void setOrderDate(LocalDateTime orderDate) {
			this.orderDate = orderDate;
		}
		public OrderStatus getOrderStatus() {
		    return orderStatus;
		}

		public void setOrderStatus(OrderStatus orderStatus) {
		    this.orderStatus = orderStatus;
		}

		public double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public String getPaymentMode() {
			return paymentMode;
		}

		public void setPaymentMode(String paymentMode) {
			this.paymentMode = paymentMode;
		}

		public String getPaymentStatus() {
			return paymentStatus;
		}

		public void setPaymentStatus(String paymentStatus) {
			this.paymentStatus = paymentStatus;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		

	    
	    
	}


