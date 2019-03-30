package com.revolut.bank;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.util.Arrays.asList;
import static spark.Spark.awaitInitialization;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.revolut.bank.domain.Account;
import com.revolut.bank.domain.Money;
import com.revolut.bank.dto.StandardResponse;
import com.revolut.bank.dto.StatusResponse;
import com.revolut.bank.dto.TransferRequestDTO;
import com.revolut.bank.exception.AccountNotFoundException;
import com.revolut.bank.exception.InsufficientBalanceException;
import com.revolut.bank.exception.InvalidMoneyTransferException;
import com.revolut.bank.repository.AccountRepository;
import com.revolut.bank.repository.InMemoryAccountRepository;
import com.revolut.bank.service.AccountService;
import com.revolut.bank.utils.JsonUtils;

public class BankApplication {

	private final Logger logger = LoggerFactory.getLogger(BankApplication.class);
	private static final Gson GSON = new Gson();

	private static final AccountRepository repository = new InMemoryAccountRepository(
			asList(new Account("revolut00001", Money.of(200)), new Account("revolut00002", Money.of(100)),
					new Account("revolut00003", Money.of(1000)), new Account("revolut00004", Money.of(2000))));

	private static final AccountService accountService = new AccountService(repository);

	private static final int PORT = 8080;

	public static void main(String[] args) {
		BankApplication bankApplication = new BankApplication();

		bankApplication.run();
	}

	private void run() {

		port(PORT);

		get("/account/:id/details", (request, response) -> {
			response.type("application/json");
			String id = request.params(":id");
			try {
				Account account = accountService.findAccountDetails(id);
				return new StandardResponse(StatusResponse.SUCCESS, null, account);
			} catch (AccountNotFoundException e) {
				response.status(HTTP_NOT_FOUND);
				return new StandardResponse(StatusResponse.ERROR, "Account not found", null);
			}

		}, JsonUtils::toJson);

		post("/account/transferMoney", (request, response) -> {
			response.type("application/json");
			TransferRequestDTO transferRequestDTO = GSON.fromJson(request.body(), TransferRequestDTO.class);

			try {
				accountService.transferMoney(transferRequestDTO.getSender(), transferRequestDTO.getReceiver(),
						Money.of(transferRequestDTO.getAmount()));
				return new StandardResponse(StatusResponse.SUCCESS, "Transaction Completed", null);
			} catch (AccountNotFoundException e) {
				response.status(HTTP_NOT_FOUND);
				return new StandardResponse(StatusResponse.ERROR, "Account not found", null);
			} catch (InsufficientBalanceException e) {
				response.status(HTTP_FORBIDDEN);
				return new StandardResponse(StatusResponse.ERROR, "Insufficient balance", null);
			} catch (InvalidMoneyTransferException e) {
				response.status(HTTP_BAD_REQUEST);
				return new StandardResponse(StatusResponse.ERROR, "Cannot transfer from account to itself", null);
			}

		}, JsonUtils::toJson);

		awaitInitialization();
		logMessage();
	}

	private void logMessage() {
		logger.info("***************************************");
		logger.info("*** Bank server is running on :{} ***", PORT);
		logger.info("***************************************");
	}

}
