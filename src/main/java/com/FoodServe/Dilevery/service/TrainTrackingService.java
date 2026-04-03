package com.FoodServe.Dilevery.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.FoodServe.Dilevery.Userrepository.TrainRepository;
import com.FoodServe.Dilevery.entity.Train;

@Service
public class TrainTrackingService {

    private final TrainRepository trainRepository;

    public TrainTrackingService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public Train addTrain(Train train) {
        return trainRepository.save(train);
    }

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    public Train getTrain(String trainNumber) {
        return trainRepository.findById(trainNumber)
                .orElseThrow(() -> new RuntimeException("Train not found"));
    }

    public Train updateStatus(String trainNumber, String status) {
        Train train = getTrain(trainNumber);
        train.setStatus(status);
        return trainRepository.save(train);
    }
    
    public void deleteTrain(String trainNumber) {
        trainRepository.deleteById(trainNumber);
    }
    
    
}