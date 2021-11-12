package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private String authToken = "";
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

    public UserService(String url) {
        this.baseUrl = url;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BigDecimal getCurrentBalance(Long id) {
        BigDecimal balance;
        try {
            balance = restTemplate.exchange(baseUrl + "user/" + id + "/balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
        } catch (ResourceAccessException | RestClientResponseException e) {
            balance = null;
            System.out.println("Failed to get balance.");
        }
        return balance;
    }

    public Long getAccountIdByUserId(Long id) {
        Long accountId;
        try {
            accountId = restTemplate.exchange(baseUrl + "user/" + id + "/accountId", HttpMethod.GET, makeAuthEntity(), Long.class).getBody();
        } catch (ResourceAccessException | RestClientResponseException e) {
            accountId = null;
            System.out.println("Failed to get account ID.");
        }
        return accountId;
    }

    public User[] getUserList() {
        User[] userList = null;
        try {
            userList = restTemplate.exchange(baseUrl + "user", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        } catch (ResourceAccessException | RestClientResponseException e) {
            userList = null;
            System.out.println("Failed to get list.");
        }
        return userList;
    }

    public Long[] getUserIdList() {
        User[] userList = null;
        try {
            userList = restTemplate.exchange(baseUrl + "user", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        } catch (ResourceAccessException | RestClientResponseException e) {
            userList = null;
            System.out.println("Failed to get list.");
        }
        Long[] userIdList = new Long[userList.length];
        for (int i = 0; i < userList.length; i++) {
            userIdList[i] = userList[i].getId();
        }
        return userIdList;
    }

    public String findUsernameByUserId(User[] userList, Long userId) {
        String usernameAssociatedWithUserId = "";
        for(User user : userList) {
            if (user.getId().equals(userId)) {
                usernameAssociatedWithUserId = user.getUsername();
            }
        }
        return usernameAssociatedWithUserId;
    }

}
