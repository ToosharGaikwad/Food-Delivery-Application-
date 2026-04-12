package com.FoodServe.Dilevery.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    // ✅ Get all products
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Product> getAllProduct() {
        return productService.getAllProduct();
    }
    // 👨‍💼 ADMIN ONLY
   
//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return Map.of("message", "Deleted");
    }
    
    
    @GetMapping("/{restaurantId}")
    public List<Product> getProductsByRestaurant(
            @PathVariable("restaurantId") Long restaurantId) {
        return productService.getProductsByRestaurant(restaurantId);
    }
}
