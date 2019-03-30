package com.revolut.bank.service;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.revolut.bank.domain.Account;
import com.revolut.bank.domain.Money;
import com.revolut.bank.exception.AccountNotFoundException;
import com.revolut.bank.repository.AccountRepository;
import com.revolut.bank.repository.InMemoryAccountRepository;

public class AccountServiceTest {

	@Test
	public void shouldTransferMoneyBetweenAccounts() {

		AccountRepository accountRepository = new InMemoryAccountRepository(
				asList(new Account("revolut00001", Money.of(200)), new Account("revolut00002", Money.of(100))));

		AccountService service = new AccountService(accountRepository);

		service.transferMoney("revolut00001", "revolut00002", Money.of(100));

		assertEquals(BigDecimal.valueOf(100), service.findAccountDetails("revolut00001").getBalance().amount());
		assertEquals(BigDecimal.valueOf(200), service.findAccountDetails("revolut00002").getBalance().amount());
	}

	@Test
	public void shouldHandleMissingSenderAccounts() {
		AccountRepository accountRepository = new InMemoryAccountRepository(
				asList(new Account("revolut00001", Money.of(200)), new Account("revolut00002", Money.of(100))));
		AccountService service = new AccountService(accountRepository);

		assertThrows(AccountNotFoundException.class,
				() -> service.transferMoney("unknown_sender", "revolut00001", Money.of(100)));

		assertThrows(AccountNotFoundException.class,
				() -> service.transferMoney("revolut00001", "unknown_receiver", Money.of(100)));

	}

	@Test
	public void shouldfindAccountDetails() {
		InMemoryAccountRepository repository = new InMemoryAccountRepository(
				asList(new Account("revolut00001", Money.of(200))));
		AccountService service = new AccountService(repository);

		assertEquals("revolut00001", service.findAccountDetails("revolut00001").getId());
	}

	@Test
	public void shouldHandleMissingAccountFindAccountBalance() {
		InMemoryAccountRepository repository = new InMemoryAccountRepository(
				asList(new Account("revolut00001", Money.of(200))));
		AccountService service = new AccountService(repository);

		assertThrows(AccountNotFoundException.class, () -> service.findAccountDetails("unknown_account"));
	}

}
