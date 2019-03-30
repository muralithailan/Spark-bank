package com.revolut.bank.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


public class MoneyTest {

	@Test
	void shouldCreateMoneyWithDefaultCurrency() {
		assertEquals(Money.DEFAULT_CURRENCY,Money.of(100).currency());
	}

	@Test
	void shouldRejectInvalidInput() {
		assertThrows(IllegalArgumentException.class, () -> Money.of(-1));
	}

	@Test
	void shouldAddAndSubtractMoney() {
		assertEquals(Money.of(100).add(Money.of(200)), Money.of(300));
		assertEquals(Money.of(200).subtract(Money.of(100)), Money.of(100));
	}
}
