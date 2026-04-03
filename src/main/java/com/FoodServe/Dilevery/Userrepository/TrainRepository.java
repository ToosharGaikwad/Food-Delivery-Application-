package com.FoodServe.Dilevery.Userrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FoodServe.Dilevery.entity.Train;

public interface TrainRepository extends JpaRepository<Train, String>{
	
}
