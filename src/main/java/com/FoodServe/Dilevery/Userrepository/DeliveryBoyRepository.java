package com.FoodServe.Dilevery.Userrepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FoodServe.Dilevery.entity.DeliveryBoyEntity;

public interface DeliveryBoyRepository extends JpaRepository<DeliveryBoyEntity, Long> {

    // ✅ Fixed method name
    List<DeliveryBoyEntity> findByAvailableTrue();
}