package com.revolut.bank.service;

import java.util.Optional;

import com.revolut.bank.domain.Account;
import com.revolut.bank.domain.Money;
import com.revolut.bank.exception.AccountNotFoundException;
import com.revolut.bank.exception.InvalidMoneyTransferException;
import com.revolut.bank.repository.AccountRepository;

public class AccountService {

	private AccountRepository accountRepository;

	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public Account findAccountDetails(String accountId) {
		Optional<Account> account = accountRepository.findById(accountId);
		if (account.isPresent()) {
			return account.get();
		}
		throw new AccountNotFoundException();
	}

	public void transferMoney(String sender, String receiver, Money amount) {
		Optional<Account> senderAccount = accountRepository.findById(sender);
		Optional<Account> receiverAccount = accountRepository.findById(receiver);

		if (!senderAccount.isPresent() || !receiverAccount.isPresent()) {
			throw new AccountNotFoundException();
		} else if (senderAccount.get().equals(receiverAccount.get())) {
			throw new InvalidMoneyTransferException();
		}

		Account firstLock;
		Account secondLock;
		if (senderAccount.get().getId().compareTo(receiverAccount.get().getId()) < 0) {
			firstLock = senderAccount.get();
			secondLock = receiverAccount.get();
		} else {
			firstLock = receiverAccount.get();
			secondLock = senderAccount.get();
		}

		synchronized (firstLock) {
			synchronized (secondLock) {
				senderAccount.get().debit(amount);
				receiverAccount.get().credit(amount);
			}
		}

	}

}
