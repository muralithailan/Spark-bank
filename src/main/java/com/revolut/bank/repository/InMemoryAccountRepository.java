package com.revolut.bank.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.Validate;

import com.revolut.bank.domain.Account;
/**
 * In-memory data store implementation using hashmap
 * @author Bala
 *
 */
public class InMemoryAccountRepository implements AccountRepository {

	private final Map<String, Account> accountsStorage = new ConcurrentHashMap<>();

	public InMemoryAccountRepository(List<Account> accounts) {
		save(accounts);
	}

	public InMemoryAccountRepository() {

	}

	@Override
	public Optional<Account> findById(String id) {
		return Optional.ofNullable(accountsStorage).map(accounts -> accounts.get(id));
	}

	@Override
	public void save(Account account) {
		Validate.isTrue(account != null);
		accountsStorage.put(account.getId(), account);
	}

	@Override
	public void save(List<Account> accounts) {
		Validate.isTrue(accounts != null);

		accounts.stream().forEach(account -> {
			accountsStorage.put(account.getId(), account);
		});

	}
}
