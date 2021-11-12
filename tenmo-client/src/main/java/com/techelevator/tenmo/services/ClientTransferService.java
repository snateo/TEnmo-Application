package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class ClientTransferService implements TransferService {

    private String authToken = "";
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

    public ClientTransferService(String url) {
        this.baseUrl = url;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }




    @Override
    public void createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        try {
            HttpEntity<Transfer> request = new HttpEntity<>(transfer);
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(baseUrl + "transfer", HttpMethod.POST, request, Transfer.class);
            //assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
            //Transfer responseTransfer = response.getBody();
        } catch (ResourceAccessException | RestClientResponseException e) {
            System.out.println("Failed to create transfer.");
        }
    }


    @Override
    public Transfer[] getTransfersFromUserId(AuthenticatedUser authenticatedUser, int userId) {
        Transfer[] transferList = null;
        try {
            transferList = restTemplate.exchange(baseUrl + "user/" + userId + "/transfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
        } catch (ResourceAccessException | RestClientResponseException e) {
            transferList = null;
            System.out.println("Failed to get list.");
        }
        return transferList;
    }

    @Override
    public Transfer getTransferFromTransferId(AuthenticatedUser authenticatedUser, int id) {
        return null;
    }

    @Override
    public Transfer[] getAllTransfers(AuthenticatedUser authenticatedUser) {
        return new Transfer[0];
    }

    @Override
    public Transfer[] getPendingTransfersByUserId(AuthenticatedUser authenticatedUser) {
        return new Transfer[0];
    }

    @Override
    public void updateTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {

    }

    @Override
    public Transfer getTransferStatus(AuthenticatedUser authenticatedUser, String description) {
        return null;
    }

    @Override
    public Transfer getTransferStatusById(AuthenticatedUser authenticatedUser, int transferStatusId) {
        return null;
    }
}
