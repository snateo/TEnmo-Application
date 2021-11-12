package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
//@PreAuthorize("isAuthenticated()")
public class UserController {


    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;

    public UserController(UserDao userDao, AccountDao accountDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/user/{id}/balance", method = RequestMethod.GET)
    public double getBalance(@PathVariable Long id) {
        return userDao.getBalance(id).doubleValue();
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public List getUserList() {
        return userDao.findAll();
    }

    @RequestMapping(path = "/user/{id}/accountId", method = RequestMethod.GET)
    public Long getAccountIdByUserId (@PathVariable int id) {
        return accountDao.getAccountIDByUserID(id);
    }

    @RequestMapping(path = "/user/{id}/transfers", method = RequestMethod.GET)
    public List getUserTransactions(@PathVariable int id) {
        int accountId = Math.toIntExact(getAccountIdByUserId(id));
        return transferDao.getTransfersByAccountId(accountId);
    }

    @RequestMapping(path = "/account/{id}/username", method = RequestMethod.GET)
    public String getUsernameByAccountId (@PathVariable int id) { return accountDao.getUsernameByAccountId(id); }



//    @ResponseStatus(HttpStatus.CREATED)
//    @RequestMapping(path="/transfers/{id}", method = RequestMethod.POST)
//    public void addTransfer(@RequestBody Transfer transfer, @PathVariable int id) {
//
//        BigDecimal amountToTransfer = transfer.getAmount();
//        Account accountFrom = accountDao.getAccountByAccountID(transfer.getAccountFrom());
//        Account accountTo = accountDao.getAccountByAccountID(transfer.getAccountTo());
//
//        //accountFrom.getBalance().sendMoney(amountToTransfer);
//        //accountTo.getBalance().receiveMoney(amountToTransfer);
//
//        transferDao.createTransfer(transfer);
//
//        accountDao.updateAccount(accountFrom);
//        accountDao.updateAccount(accountTo);
//}
}
