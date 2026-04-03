package com.FoodServe.Dilevery.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.FoodServe.Dilevery.Userrepository.RestaurantRepository;
import com.FoodServe.Dilevery.entity.Restaurant;

@Service
public class RestaurentServiceImpl implements RestaurantService{

	
	 private final RestaurantRepository restaurantRepository;
	 
	 
	public RestaurentServiceImpl(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	@Override
	public Restaurant saveRestaurant(Restaurant restaurant) {
		return restaurantRepository.save(restaurant);
	}

	@Override
	public List<Restaurant> getAllRestaurant() {		
		return restaurantRepository.findAll();
	}
	@Override
	public Restaurant getRestaurantById(Long id) {

	    return restaurantRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Restaurant not found with id " + id));
	}

	public void deleteRestaurant(Long id) {
	    restaurantRepository.deleteById(id);
	}
	 
}
