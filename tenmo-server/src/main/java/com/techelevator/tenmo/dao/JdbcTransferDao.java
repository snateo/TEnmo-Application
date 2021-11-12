package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao() {
    }

    @Override
    public void createTransfer(Transfer transfer) {
        //insert new transfer into transfer table
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (" + transfer.getTransferTypeId() + ", " + transfer.getTransferStatusId() + ", " +
                transfer.getAccountFrom() + ", " + transfer.getAccountTo() + ", " + transfer.getAmount() + ");";
        jdbcTemplate.update(sql);
        //return transfer;
    }

    @Override
    public List<Transfer> getTransfersByAccountId(int accountId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfers WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (results.next()){
            transferList.add(mapRowToTransfer(results));
        }
        return transferList;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT * FROM transfers WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()){
            return mapRowToTransfer((results));
        }
        else {
            System.out.println("Transfer not found! Help!");
            return transfer;
        }
    }

    @Override
    public List<Transfer> getAllTransfers() {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfers;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()){
            transferList.add(mapRowToTransfer(results));
        }
        return transferList;
    }

    @Override
    public List<Transfer> getPendingTransfers(int userId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfers WHERE status_id = 1 AND user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()){
            transferList.add(mapRowToTransfer(results));
        }
        return transferList;
    }

    @Override
    public void updateTransfer(Transfer transfer) {

    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getLong("account_from"));
        transfer.setAccountTo(rs.getLong("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
