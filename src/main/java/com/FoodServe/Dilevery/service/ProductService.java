package com.FoodServe.Dilevery.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.FoodServe.Dilevery.Userrepository.ProductRepository;
import com.FoodServe.Dilevery.Userrepository.RestaurantRepository;
import com.FoodServe.Dilevery.entity.Product;
import com.FoodServe.Dilevery.entity.Restaurant;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;

    public ProductService(ProductRepository productRepository,
                          RestaurantRepository restaurantRepository) {
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
    }
    public List<Product> getProductsByRestaurant(Long restaurantId) {
        return productRepository.findByRestaurantId(restaurantId);
    }
    // ✅ Get one product by id
    public Product oneProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not available"));
    }

    // ✅ Get all products
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    // ✅ Add product (FIXED)
    public Product addProduct(Product product) {

        // 🔴 VALIDATION
        if (product.getRestaurant() == null || product.getRestaurant().getId() == null) {
            throw new RuntimeException("Restaurant id is required");
        }

        Long restaurantId = product.getRestaurant().getId();

        // ✅ LOAD MANAGED RESTAURANT
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // 🔥 ATTACH IT
        product.setRestaurant(restaurant);

        return productRepository.save(product);
    }
	public void deleteProduct(Long Id) {
		productRepository.deleteById(Id);
		
	}
	
}