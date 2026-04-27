package com.FoodServe.Dilevery.dto;


import java.util.List;

public class OrderRequestDTO {
	private String status;
    private Long userId;
    private String paymentMode;
    private List<OrderItemRequestDTO> items;

    // getters & setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

	public List<OrderItemRequestDTO> getItems() {
		return items;
	}

	public void setItems(List<OrderItemRequestDTO> items) {
		this.items = items;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

    
}