package it.iacovelli.grocerybe.service;

import it.iacovelli.grocerybe.model.dto.TransactionDto;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    TransactionDto addTransaction(TransactionDto transactionDto, UUID itemId, String userId);

    List<TransactionDto> getItemTransactions(UUID itemId, boolean onlyAvailable, String userId);

    TransactionDto getItemTransaction(UUID itemId, UUID transactionId, String userId);

    TransactionDto updateItemTransaction(UUID itemId, TransactionDto transactionDto, String userId);

    void deleteItemTransaction(UUID itemId, UUID transactionId, String userId);

}
