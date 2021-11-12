package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class ClientAccountService implements AccountService {

    private String authToken = "";
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

    public ClientAccountService(String url) {
        this.baseUrl = url;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public Account getBalance(AuthenticatedUser authenticatedUser) {
        return null;
    }

    @Override
    public Account getAccountByUserId(AuthenticatedUser authenticatedUser, Long userId) {

        return null;
    }

    @Override
    public Account getAccountById(AuthenticatedUser authenticatedUser, Long accountId) {
        return null;
    }

    public void updateAccountBalance(AuthenticatedUser authenticatedUser, Account account) {
        try {
            HttpEntity<Account> request = new HttpEntity<>(account);
            ResponseEntity<Account> response =
                    restTemplate.exchange(baseUrl + "account/" + account.getUserId(), HttpMethod.PUT, request, Account.class);
        } catch (ResourceAccessException | RestClientResponseException e) {
            System.out.println("Failed to update balance.");
        }
    }



    public String getUsernameByAccountId(Long id) {
        String username;
        try {
            username = restTemplate.exchange(baseUrl + "account/" + id + "/username", HttpMethod.GET, makeAuthEntity(), String.class).getBody();
        } catch (ResourceAccessException | RestClientResponseException e) {
            username = null;
            System.out.println("Failed to get account ID.");
        }
        return username;
    }
}

