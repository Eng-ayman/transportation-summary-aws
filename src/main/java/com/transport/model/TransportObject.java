package com.transport.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransportObject {

	// general property
	@JsonProperty("model")
	private String model;

	// ------------------------------ car properties ----------------------------
	@JsonProperty("manufacturer")
	private String manufacturer;

	@JsonProperty("passenger-capacity")
	private int passengerCapacity;

	// ------------------------ train properties ------------

	@JsonProperty("number-wagons")
	private int wagonsNumber;

	@JsonProperty("w-passenger-capacity")
	private int wagonPassengerCapacity;

	// ------------------------ plane properties ------------

	@JsonProperty("b-passenger-capacity")
	private int bussinessPassengerCapacity;

	@JsonProperty("e-passenger-capacity")
	private int economyPassengerCapacity;

	private TransportType transportType;

	
	
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getPassengerCapacity() {
		return passengerCapacity;
	}

	public void setPassengerCapacity(int passengerCapacity) {
		this.passengerCapacity = passengerCapacity;
		this.transportType=TransportType.CAR;
	}

	public int getWagonsNumber() {
		return wagonsNumber;
	}

	public void setWagonsNumber(int wagonsNumber) {
		this.wagonsNumber = wagonsNumber;
		this.transportType=TransportType.TRAIN;
		
	}

	public int getWagonPassengerCapacity() {
		return wagonPassengerCapacity;
	}

	public void setWagonPassengerCapacity(int wagonPassengerCapacity) {
		this.wagonPassengerCapacity = wagonPassengerCapacity;
		this.transportType=TransportType.TRAIN;
	}

	public int getBussinessPassengerCapacity() {
		return bussinessPassengerCapacity;
	}

	public void setBussinessPassengerCapacity(int bussinessPassengerCapacity) {
		this.bussinessPassengerCapacity = bussinessPassengerCapacity;
		this.transportType=TransportType.PLANE;
		
	}

	public int getEconomyPassengerCapacity() {
		return economyPassengerCapacity;
	}

	public void setEconomyPassengerCapacity(int economyPassengerCapacity) {
		this.economyPassengerCapacity = economyPassengerCapacity;
		this.transportType=TransportType.PLANE;
	}

	public TransportType getTransportType() {
		return transportType;
	}

	public void setTransportType(TransportType transportType) {
		this.transportType = transportType;
	}

}