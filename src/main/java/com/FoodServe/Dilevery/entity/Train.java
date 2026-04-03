package com.FoodServe.Dilevery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Train")
public class Train {
	@Id
	@Column(name = "trainNumber")
	String trainNumber;
	@Column(name="currentStation")
	String currentStation;
	@Column(name="destination")
	String destination;
	@Column(name="status")
	String status;
	@Column(name="trainName")
	String trainName;
	@Column(name="source")
	String source;
	
	public Train() {
	}

	public Train(String trainNumber, String currentStation, String destination, String status, String trainName,
			String source) {
		super();
		this.trainNumber = trainNumber;
		this.currentStation = currentStation;
		this.destination = destination;
		this.status = status;
		this.trainName = trainName;
		this.source = source;
	}

	public String getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}

	public String getCurrentStation() {
		return currentStation;
	}

	public void setCurrentStation(String currentStation) {
		this.currentStation = currentStation;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTrainName() {
		return trainName;
	}

	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	
	
}
