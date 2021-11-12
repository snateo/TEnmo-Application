package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;


public interface AccountDao {

    Account getAccountByUserID(int userId);
    Account getAccountByAccountID(int accountId);
    void setBalance(double newBalance, int userId);
    void updateAccountBalance(Account account);
    Long getAccountIDByUserID(int userId);
    String getUsernameByAccountId(int id);

}