package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    void createTransfer(Transfer transfer);

    List<Transfer> getTransfersByAccountId(int accountId);

    Transfer getTransferByTransferId(int transferId);

    List<Transfer> getAllTransfers();

    List<Transfer> getPendingTransfers(int userId);

    void updateTransfer(Transfer transfer);
}