package com.FoodServe.Dilevery.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.FoodServe.Dilevery.service.TrainService;
@RestController
@RequestMapping("/api/train")
public class TrainController {

    private final TrainService service;

    public TrainController(TrainService service) {
        this.service = service;
    }

    @GetMapping("/live-station")
    public String getLiveStation(
            @RequestParam("fromStationCode") String fromStationCode,
            @RequestParam("hours") int hours) {

        return service.getLiveStation(fromStationCode, hours);
    }
}
