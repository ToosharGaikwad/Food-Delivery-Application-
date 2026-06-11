package com.FoodServe.Dilevery.service;

import java.util.List;

import com.FoodServe.Dilevery.entity.Restaurant;

public interface RestaurantService {

    Restaurant saveRestaurant(Restaurant restaurant);

    List<Restaurant> getAllRestaurant();

    Restaurant getRestaurantById(Long id);

    void deleteRestaurant(Long id);
    Restaurant updateRestaurant(Long id, Restaurant restaurantDetail);
}