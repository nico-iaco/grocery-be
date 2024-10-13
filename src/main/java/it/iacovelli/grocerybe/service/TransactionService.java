package it.iacovelli.grocerybe.service;

import it.iacovelli.grocerybe.model.dto.TransactionDto;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    TransactionDto addTransaction(TransactionDto transactionDto, UUID itemId, UUID pantryId);

    List<TransactionDto> getItemTransactions(UUID itemId, boolean onlyAvailable, UUID pantryId);

    TransactionDto getItemTransaction(UUID itemId, UUID transactionId, UUID pantryId);

    TransactionDto updateItemTransaction(UUID itemId, TransactionDto transactionDto, UUID pantryId);

    void deleteItemTransaction(UUID itemId, UUID transactionId, UUID pantryId);

}
