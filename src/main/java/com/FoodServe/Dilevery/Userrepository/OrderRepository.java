package com.FoodServe.Dilevery.Userrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FoodServe.Dilevery.entity.OrdersEntity;

public interface OrderRepository extends JpaRepository<OrdersEntity,Long>{
	
}
