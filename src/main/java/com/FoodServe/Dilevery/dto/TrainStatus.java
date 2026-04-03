package com.FoodServe.Dilevery.dto;
public class TrainStatus {

    private String currentStation;
    private String nextStation;
    private String eta;

    // No-arg constructor (IMPORTANT)
    public TrainStatus() {
    }

    // All-args constructor
    public TrainStatus(String currentStation, String nextStation, String eta) {
        this.currentStation = currentStation;
        this.nextStation = nextStation;
        this.eta = eta;
    }

    public String getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(String currentStation) {
        this.currentStation = currentStation;
    }

    public String getNextStation() {
        return nextStation;
    }

    public void setNextStation(String nextStation) {
        this.nextStation = nextStation;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }
}
