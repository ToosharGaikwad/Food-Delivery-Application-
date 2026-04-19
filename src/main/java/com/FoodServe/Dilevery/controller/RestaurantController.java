package com.FoodServe.Dilevery.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FoodServe.Dilevery.entity.Restaurant;
import com.FoodServe.Dilevery.service.RestaurentServiceImpl;
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/res")
@RestController
public class RestaurantController {
	
	private final RestaurentServiceImpl restaurantServiceimpl;

	public RestaurantController( RestaurentServiceImpl restaurantServiceimpl) {
		this.restaurantServiceimpl = restaurantServiceimpl;
	}
	
//	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addRestaurant")
	public Restaurant saveRestaurant(@RequestBody Restaurant restaurant)
	{
		return restaurantServiceimpl.saveRestaurant(restaurant);
	}
	
//	@PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_MAN','USER")
	@GetMapping("/allRestaurant")
	public List<Restaurant> getAllRestorant()
	{
		return restaurantServiceimpl.getAllRestaurant();
	}
	
	@GetMapping("/{id}")
	public Restaurant getRestaurantByid(@PathVariable Long id)
	{
		return restaurantServiceimpl.getRestaurantById(id);
	}
	
//	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/id/{id}")
	public String deleteRestaurant(@PathVariable Long id) {
		restaurantServiceimpl.deleteRestaurant(id);
	    return "Restaurant deleted successfully";
	}
	

}
