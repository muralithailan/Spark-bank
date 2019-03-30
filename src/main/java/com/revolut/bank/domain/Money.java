package com.revolut.bank.domain;

import java.util.Currency;

import org.apache.commons.lang3.Validate;

import static java.util.Locale.UK;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import java.math.BigDecimal;

public class Money {

	public static final Currency DEFAULT_CURRENCY = Currency.getInstance(UK);

	private BigDecimal amount;
	private Currency currency;

	private Money(BigDecimal amount, Currency currency) {
		this.amount = amount;
		this.currency = currency;
	}

	public Currency currency() {
		return currency;
	}

	public BigDecimal amount() {
		return amount;
	}

	public static Money of(long value) {
		return Money.of(BigDecimal.valueOf(value), DEFAULT_CURRENCY);
	}

	public static Money of(BigDecimal value) {
		return Money.of(value, DEFAULT_CURRENCY);
	}

	public static Money of(BigDecimal value, Currency currency) {
		Validate.isTrue(value.signum() >= 0);
		return new Money(value, currency);
	}

	public Money add(Money money) {
		return Money.of(this.amount.add(money.amount));
	}

	public Money subtract(Money money) {
		return Money.of(this.amount.subtract(money.amount));
	}

	public boolean isLessThan(Money money) {
		return this.amount.subtract(money.amount).signum() < 0;
	}

	@Override
	public boolean equals(Object money) {
		return reflectionEquals(this, money);
	}

	@Override
	public int hashCode() {
		return reflectionHashCode(this);
	}
}
