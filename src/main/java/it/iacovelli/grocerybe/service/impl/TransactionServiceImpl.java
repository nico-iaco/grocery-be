package it.iacovelli.grocerybe.service.impl;

import it.iacovelli.grocerybe.exception.ItemNotFoundException;
import it.iacovelli.grocerybe.mapper.TransactionMapper;
import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.Transaction;
import it.iacovelli.grocerybe.model.dto.TransactionDto;
import it.iacovelli.grocerybe.repository.ItemRepository;
import it.iacovelli.grocerybe.repository.TransactionRepository;
import it.iacovelli.grocerybe.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final ItemRepository itemRepository;

    private final TransactionMapper transactionMapper;

    @Override
    @Transactional
    public TransactionDto addTransaction(TransactionDto transactionDto, UUID itemId) {
        Item item = getItemFromId(itemId);
        Transaction transaction = transactionMapper.dtoToEntity(transactionDto, item);
        item.getTransactionList().add(transaction);
        return transactionMapper.entityToDto(transactionRepository.save(transaction));
    }

    @Override
    public List<TransactionDto> getItemTransactions(UUID itemId) {
        Item item = getItemFromId(itemId);
        return transactionRepository.findTransactionsByItem(item).stream().map(transactionMapper::entityToDto).toList();
    }

    @Override
    @Transactional
    public TransactionDto updateItemTransaction(UUID itemId, TransactionDto transactionDto) {
        Item item = getItemFromId(itemId);
        Transaction transaction = transactionRepository.findTransactionByIdAndItem(transactionDto.getId(), item)
                .orElseThrow(() -> new RuntimeException("The transaction was not found for the item"));
        transactionMapper.updateTransaction(transactionDto, transaction);
        return transactionMapper.entityToDto(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public void deleteItemTransaction(UUID itemId, UUID transactionId) {
        Item item = getItemFromId(itemId);
        Transaction transaction = transactionRepository.findTransactionByIdAndItem(transactionId, item)
                .orElseThrow(() -> new RuntimeException("The transaction was not found for the item"));
        item.getTransactionList().remove(transaction);
        transactionRepository.delete(transaction);
        itemRepository.save(item);
    }

    private Item getItemFromId(UUID itemId) {
        return itemRepository.findItemById(itemId).orElseThrow(() -> new ItemNotFoundException("The item was not found"));
    }
}
