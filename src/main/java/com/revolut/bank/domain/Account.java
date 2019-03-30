package com.revolut.bank.domain;

import org.apache.commons.lang3.Validate;

import com.revolut.bank.exception.InsufficientBalanceException;

public class Account {

	private String id;
	private Money balance;

	public Account(String id, Money balance) {
		Validate.notBlank(id);
		Validate.notNull(balance);
		this.id = id;
		this.balance = balance;
	}

	public String getId() {
		return id;
	}

	public Money getBalance() {
		return this.balance;
	}

	public void credit(Money amount) {
		this.balance = this.balance.add(amount);
	}

	public void debit(Money money) {
		if (this.balance.isLessThan(money)) {
			throw new InsufficientBalanceException();
		}
		this.balance = this.balance.subtract(money);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
