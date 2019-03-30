package com.revolut.bank.repository;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.revolut.bank.domain.Account;
import com.revolut.bank.domain.Money;

public class InMemoryAccountRepositoryTest {

	private AccountRepository accountRepository = new InMemoryAccountRepository(
			asList(new Account("revolut00001", Money.of(200)), new Account("revolut00002", Money.of(100))));

	@Test
	void shouldRejectInvalidInput() {
		assertThrows(IllegalArgumentException.class, () -> new InMemoryAccountRepository(null));
	}

	@Test
	void shouldFindAccountById() {
		assertTrue(accountRepository.findById("revolut00001").isPresent());
		assertFalse(accountRepository.findById("revolut00100").isPresent());
	}
}
