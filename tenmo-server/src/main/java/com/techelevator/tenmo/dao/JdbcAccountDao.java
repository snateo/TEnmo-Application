package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Account getAccountByUserID(int userId) {
        String sql = "SELECT user_id, account_id, balance FROM accounts WHERE user_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        if (rowSet.next()){
            return mapRowToAccount(rowSet);
        }
        else {
            System.out.println("ERROR: UserID not found.");
            //Throw an exception?
            return null;
        }
    }

    @Override
    public Long getAccountIDByUserID(int userId) {
        String sql = "SELECT account_id FROM accounts WHERE user_id = ?;";
        return jdbcTemplate.queryForObject(sql, Long.class, userId);
    }

    @Override
    public Account getAccountByAccountID(int accountId) {
        String sql = "SELECT user_id, account_id, balance FROM accounts WHERE account_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
        if (rowSet.next()){
            return mapRowToAccount(rowSet);
        }
        else {
            System.out.println("ERROR: Account ID not found.");
            //Throw an exception?
            return null;
        }
    }

    public void updateAccountBalance(Account account) {
        String sql = "UPDATE accounts SET balance = " + account.getBalance()
                + " WHERE user_id = " + account.getUserId();
        jdbcTemplate.update(sql);
    }


        /*

    @Override
    public void createTransfer(Transfer transfer) {
        //insert new transfer into transfer table
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (" + transfer.getTransferTypeId() + ", " + transfer.getTransferStatusId() + ", " +
                transfer.getAccountFrom() + ", " + transfer.getAccountTo() + ", " + transfer.getAmount() + ");";
        jdbcTemplate.update(sql);
        //return transfer;
    }


     */

    public String getUsernameByAccountId(int id) {
        String sql = "SELECT username FROM users JOIN accounts USING (user_id) WHERE account_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    public void setBalance(double newBalance, int userId) {
        String sql = "UPDATE accounts SET balance = ? WHERE userId = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, newBalance, userId);
    }


    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getLong("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }


}
