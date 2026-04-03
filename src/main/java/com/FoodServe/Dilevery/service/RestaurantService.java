package com.FoodServe.Dilevery.service;

import java.util.List;

import com.FoodServe.Dilevery.entity.Restaurant;

public interface RestaurantService {

	public Restaurant saveRestaurant(Restaurant restaurant);
	List<Restaurant> getAllRestaurant();
	Restaurant getRestaurantById(Long id);
}
