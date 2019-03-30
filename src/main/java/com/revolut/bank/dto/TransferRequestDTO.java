package com.revolut.bank.dto;

import java.math.BigDecimal;

public class TransferRequestDTO {

	private String sender;
	private String receiver;
	private BigDecimal amount;

	public TransferRequestDTO() {
	}

	public TransferRequestDTO(String sender, String receiver, BigDecimal amount) {
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
	}

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public BigDecimal getAmount() {
		return amount;
	}

}
