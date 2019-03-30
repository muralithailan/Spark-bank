package com.revolut.bank;

import static com.google.common.truth.Truth.assertThat;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;

import java.math.BigDecimal;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.revolut.bank.dto.StandardResponse;
import com.revolut.bank.dto.StatusResponse;
import com.revolut.bank.dto.TransferRequestDTO;


public class BankApplicationTest extends AbstractSparkTest {

	private static final Gson GSON = new Gson();

	@Test
	public void transferMoneyValid() throws Exception {
		HttpPost request = new HttpPost(SERVER_URL + "/account/transferMoney");
		request.setEntity(new StringEntity(
				GSON.toJson(new TransferRequestDTO("revolut00001", "revolut00002", BigDecimal.valueOf(100)))));

		CloseableHttpResponse response = client.execute(request);

		assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_OK);
		String expectedResponse = GSON
				.toJson(new StandardResponse(StatusResponse.SUCCESS, "Transaction Completed", null));
		assertResponses(expectedResponse, getResponseBodyAndClose(response));
	}
	
	@Test
    void shouldHandleMissingAccountsTransferMoney() throws Exception {
		
		HttpPost request = new HttpPost(SERVER_URL + "/account/transferMoney");
		request.setEntity(new StringEntity(
				GSON.toJson(new TransferRequestDTO("revolut00001", "Invalid_Account", BigDecimal.valueOf(100)))));

		CloseableHttpResponse response = client.execute(request);

		assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_NOT_FOUND);
		String expectedResponse = GSON
				.toJson(new StandardResponse(StatusResponse.ERROR, "Account not found", null));
		assertResponses(expectedResponse, getResponseBodyAndClose(response));
    }
	
	@Test
    void shouldHandleMissingAccountsDetails() throws Exception {
		
		HttpGet request = new HttpGet(SERVER_URL + "/account/Invalid_Account");
		
		CloseableHttpResponse response = client.execute(request);

		assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_NOT_FOUND);
		String expectedResponse = GSON
				.toJson(new StandardResponse(StatusResponse.ERROR, "Account not found", null));
		assertResponses(expectedResponse, getResponseBodyAndClose(response));
    }

    @Test
    void shouldHandleInsufficientBalance() throws Exception {
    	HttpPost request = new HttpPost(SERVER_URL + "/account/transferMoney");
		request.setEntity(new StringEntity(
				GSON.toJson(new TransferRequestDTO("revolut00001", "revolut00002", BigDecimal.valueOf(1000)))));

		CloseableHttpResponse response = client.execute(request);

		assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_FORBIDDEN);
		String expectedResponse = GSON
				.toJson(new StandardResponse(StatusResponse.ERROR, "Insufficient balance", null));
		assertResponses(expectedResponse, getResponseBodyAndClose(response));
    }
    
    @Test
    void shouldHandleInvalidMoneyTransfer() throws Exception {
    	HttpPost request = new HttpPost(SERVER_URL + "/account/transferMoney");
		request.setEntity(new StringEntity(
				GSON.toJson(new TransferRequestDTO("revolut00001", "revolut00001", BigDecimal.valueOf(100)))));

		CloseableHttpResponse response = client.execute(request);

		assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HTTP_BAD_REQUEST);
		String expectedResponse = GSON
				.toJson(new StandardResponse(StatusResponse.ERROR, "Cannot transfer from account to itself", null));
		assertResponses(expectedResponse, getResponseBodyAndClose(response));
    }
}
