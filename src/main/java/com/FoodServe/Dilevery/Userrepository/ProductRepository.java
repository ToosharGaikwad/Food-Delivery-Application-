package com.FoodServe.Dilevery.Userrepository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.FoodServe.Dilevery.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
    List<Product> findByRestaurantId(Long restaurantId);
}
