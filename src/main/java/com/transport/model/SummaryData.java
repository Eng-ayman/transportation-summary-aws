package com.transport.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SummaryData {

	@JsonProperty("cars")
	private int carsTotalCapcity;

	@JsonProperty("trains")
	private int trainsTotalCapcity;

	@JsonProperty("planes")
	private int planesTotalCapcity;

	public int getCarsTotalCapcity() {
		return carsTotalCapcity;
	}

	public void setCarsTotalCapcity(int carsTotalCapcity) {
		this.carsTotalCapcity = carsTotalCapcity;
	}

	public int getTrainsTotalCapcity() {
		return trainsTotalCapcity;
	}

	public void setTrainsTotalCapcity(int trainsTotalCapcity) {
		this.trainsTotalCapcity = trainsTotalCapcity;
	}

	public int getPlanesTotalCapcity() {
		return planesTotalCapcity;
	}

	public void setPlanesTotalCapcity(int planesTotalCapcity) {
		this.planesTotalCapcity = planesTotalCapcity;
	}

	public void accumulateToCarsTotal(int value) {
		carsTotalCapcity += value;
	}

	public void accumulateToTrainsTotal(int value) {
		trainsTotalCapcity += value;
	}

	public void accumulateToPlaneTotal(int value) {
		planesTotalCapcity += value;
	}

}
