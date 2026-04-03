package com.FoodServe.Dilevery.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.FoodServe.Dilevery.entity.Train;
import com.FoodServe.Dilevery.service.TrainTrackingService;

@RestController
@RequestMapping("/api/trains")
//@CrossOrigin
public class TrainTrackingController {

    private final TrainTrackingService trainService;

    public TrainTrackingController(TrainTrackingService trainService) {
        this.trainService = trainService;
    }
    @PostMapping
    public Train addTrain(@RequestBody Train train) {
        return trainService.addTrain(train);
    }

    
    @GetMapping("/{trainNumber}")
    public Train getTrain(@PathVariable String trainNumber) {
        return trainService.getTrain(trainNumber);
    }

    
    @GetMapping
    public List<Train> getAllTrain() {
        return trainService.getAllTrains();
    }

   
    @PutMapping("/status/{trainNumber}")
    public Train updateStatus(
            @PathVariable String trainNumber,
            @RequestParam String status) {

        return trainService.updateStatus(trainNumber, status);
    }

   
    @DeleteMapping("/{trainNumber}")
    public void deleteTrain(@PathVariable String trainNumber) {
        trainService.deleteTrain(trainNumber);
    }
}
