package com.transport.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransportData {

	@JsonProperty("transports")
	private List<TransportObject> transportRecords;

	public List<TransportObject> getTransportRecords() {
		return transportRecords;
	}

	public void setTransportRecords(List<TransportObject> transportRecords) {
		this.transportRecords = transportRecords;
	}
}
