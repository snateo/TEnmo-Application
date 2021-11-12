package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private Long accountId;
    private int userId;
    private BigDecimal balance;

    public Account() {

    }

    public Account(Long accountId, int userId) {
        this.accountId = accountId;
        this.userId = userId;

    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


//    public class Balance() {
//        private BigDecimal balance;
//        public BigDecimal getBalance() {
//            return balance;
//        }
//        public void setBalance(BigDecimal balance) {
//            this.balance = balance;
//        }
//        public void sendMoney(BigDecimal amount) {
//            BigDecimal newBalance = new BigDecimal(String.valueOf(balance)).subtract(amount);
//            if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
//                this.balance = newBalance;
//            } else {
//                System.out.println("No monies!");;
//            }
//        }
//        public void receiveMoney(BigDecimal amount) {
//            this.balance = new BigDecimal(String.valueOf(balance)).add(amount);
//        }
//    }
}