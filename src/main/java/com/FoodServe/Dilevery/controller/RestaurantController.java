package com.FoodServe.Dilevery.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FoodServe.Dilevery.entity.Restaurant;
import com.FoodServe.Dilevery.service.RestaurentServiceImpl;

@RequestMapping("/res")
@RestController
public class RestaurantController {
	
	private final RestaurentServiceImpl restaurantServiceimpl;

	public RestaurantController( RestaurentServiceImpl restaurantServiceimpl) {
		this.restaurantServiceimpl = restaurantServiceimpl;
	}
	

	@PostMapping("/addRestaurant")
	public Restaurant saveRestaurant(@RequestBody Restaurant restaurant)
	{
		return restaurantServiceimpl.saveRestaurant(restaurant);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Restaurant> updateRestaurant(
	        @PathVariable("id") Long id,
	        @RequestBody Restaurant restaurant) {

		System.out.println("called update");
	    Restaurant updatedRestaurant =
	    		restaurantServiceimpl.updateRestaurant(id, restaurant);

	    return ResponseEntity.ok(updatedRestaurant);
	}
	

	@GetMapping("/allRestaurant")
	public List<Restaurant> getAllRestorant()
	{
		return restaurantServiceimpl.getAllRestaurant();
	}
	
	@GetMapping("/{id}")
	public Restaurant getRestaurantByid(@PathVariable("id") Long id)
	{
	    return restaurantServiceimpl.getRestaurantById(id);
	}
	

	@DeleteMapping("/id/{id}")
	public Map<String, String> deleteRestaurant(@PathVariable("id") Long id) {
	    restaurantServiceimpl.deleteRestaurant(id);
	    return Map.of("message", "Restaurant deleted successfully");
	}
	

}
