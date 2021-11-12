package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;


import java.math.BigDecimal;

public interface AccountService {

    Account getBalance(AuthenticatedUser authenticatedUser);

    Account getAccountByUserId(AuthenticatedUser authenticatedUser, Long userId);

    Account getAccountById(AuthenticatedUser authenticatedUser, Long accountId);

    String getUsernameByAccountId(Long id);

    void updateAccountBalance(AuthenticatedUser authenticatedUser, Account account);

}