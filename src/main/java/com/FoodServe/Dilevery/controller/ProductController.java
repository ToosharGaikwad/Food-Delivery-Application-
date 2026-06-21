package com.FoodServe.Dilevery.controller;

import java.util.List;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FoodServe.Dilevery.entity.Product;
import com.FoodServe.Dilevery.service.ProductService;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ✅ Add product
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    // ✅ Get all products
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<Product> getAllProduct() {
        return productService.getAllProduct();
    }
    // 👨‍💼 ADMIN ONLY
   

    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return Map.of("message", "Deleted");
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id,
    		@RequestBody Product product
    		) {
    	Product udProduct = productService.updateProduct(id, product);
    	return ResponseEntity.ok(udProduct);
    }
    
    @GetMapping("/{restaurantId}")
    public List<Product> getProductsByRestaurant(
            @PathVariable("restaurantId") Long restaurantId) {
        return productService.getProductsByRestaurant(restaurantId);
    }
}
