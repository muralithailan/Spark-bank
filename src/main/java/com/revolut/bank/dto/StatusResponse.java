package com.revolut.bank.dto;

public enum StatusResponse {
	SUCCESS("Success"),ERROR("Error");
	
	private final String status;
	
	StatusResponse(String status){
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
	
}
