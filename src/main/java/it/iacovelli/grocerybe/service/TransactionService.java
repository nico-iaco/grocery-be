package it.iacovelli.grocerybe.service;

import it.iacovelli.grocerybe.model.dto.TransactionDto;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    TransactionDto addTransaction(TransactionDto transactionDto, UUID itemId);

    List<TransactionDto> getItemTransactions(UUID itemId);

    TransactionDto getItemTransaction(UUID itemId, UUID transactionId);

    TransactionDto updateItemTransaction(UUID itemId, TransactionDto transactionDto);

    void deleteItemTransaction(UUID itemId, UUID transactionId);

}
