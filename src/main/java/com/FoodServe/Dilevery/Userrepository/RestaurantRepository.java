package com.FoodServe.Dilevery.Userrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FoodServe.Dilevery.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{

}
