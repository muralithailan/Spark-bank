package com.revolut.bank.repository;

import java.util.List;
import java.util.Optional;

import com.revolut.bank.domain.Account;

public interface AccountRepository {

	Optional<Account> findById(String id);
	
	void save(Account account);
	
	void save(List<Account> save);
}
