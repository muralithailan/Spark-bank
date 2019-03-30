package com.revolut.bank.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.revolut.bank.exception.InsufficientBalanceException;

public class AccountTest {

	@Test
	void shouldCreateAccountWithInitialBalance() {
		Account account = new Account("revolut00001", Money.of(10));

		assertEquals(account.getBalance(), Money.of(10));
		assertEquals(account.getId(), "revolut00001");
	}

	@Test
	void shouldCreditMoney() {
		Account account = new Account("id123", Money.of(10));

		account.credit(Money.of(20));

		assertEquals(account.getBalance(), Money.of(30));
	}

	@Test
	void shouldDebitMoney() {
		Account account = new Account("id123", Money.of(50));

		account.debit(Money.of(10));

		assertEquals(account.getBalance(), Money.of(40));
	}

	@Test
	void shouldIdentifyInsufficientBalance() {
		Account account = new Account("id123", Money.of(50));

		assertThrows(InsufficientBalanceException.class, () -> account.debit(Money.of(51)));
	}
}
