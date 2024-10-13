package it.iacovelli.grocerybe.service.impl;

import it.iacovelli.grocerybe.exception.ItemNotFoundException;
import it.iacovelli.grocerybe.exception.PantryNotFoundException;
import it.iacovelli.grocerybe.mapper.TransactionMapper;
import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.Pantry;
import it.iacovelli.grocerybe.model.Transaction;
import it.iacovelli.grocerybe.model.dto.TransactionDto;
import it.iacovelli.grocerybe.repository.ItemRepository;
import it.iacovelli.grocerybe.repository.TransactionRepository;
import it.iacovelli.grocerybe.service.PantryService;
import it.iacovelli.grocerybe.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final ItemRepository itemRepository;

    private final TransactionMapper transactionMapper;

    private final PantryService pantryService;

    @Override
    @Transactional
    public TransactionDto addTransaction(TransactionDto transactionDto, UUID itemId, UUID pantryId) {
        Item item = getItemFromId(itemId, pantryId);
        Transaction transaction = transactionMapper.dtoToEntity(transactionDto, item);
        item.getTransactionList().add(transaction);
        return transactionMapper.entityToDto(transactionRepository.save(transaction));
    }

    @Override
    public List<TransactionDto> getItemTransactions(UUID itemId, boolean onlyAvailable, UUID pantryId) {
        Item item = getItemFromId(itemId, pantryId);
        return transactionRepository.findTransactionsByItemOrderByExpirationDateAsc(item)
                .stream()
                .filter(transaction -> !onlyAvailable || transaction.getAvailableQuantity() > 0)
                .map(transactionMapper::entityToDto)
                .toList();
    }

    @Override
    public TransactionDto getItemTransaction(UUID itemId, UUID transactionId, UUID pantryId) {
        Item item = getItemFromId(itemId, pantryId);
        Transaction transaction = transactionRepository.findTransactionByIdAndItem(transactionId, item)
                .orElseThrow(() -> new RuntimeException("The transaction was not found for the item"));
        return transactionMapper.entityToDto(transaction);
    }

    @Override
    public TransactionDto updateItemTransaction(UUID itemId, TransactionDto transactionDto, UUID pantryId) {
        Item item = getItemFromId(itemId, pantryId);
        Transaction transaction = transactionRepository.findTransactionByIdAndItem(transactionDto.getId(), item)
                .orElseThrow(() -> new RuntimeException("The transaction was not found for the item"));
        transactionMapper.updateTransaction(transactionDto, transaction);
        return transactionMapper.entityToDto(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public void deleteItemTransaction(UUID itemId, UUID transactionId, UUID pantryId) {
        Item item = getItemFromId(itemId, pantryId);
        Transaction transaction = transactionRepository.findTransactionByIdAndItem(transactionId, item)
                .orElseThrow(() -> new RuntimeException("The transaction was not found for the item"));
        item.getTransactionList().remove(transaction);
        transactionRepository.delete(transaction);
        itemRepository.save(item);
    }

    private Item getItemFromId(UUID itemId, UUID pantryId) {
        Optional<Pantry> optionalPantry = pantryService.getPantryById(pantryId);
        Pantry pantry = optionalPantry.orElseThrow(() -> new PantryNotFoundException("The pantry was not found"));
        return itemRepository.findItemByIdAndPantry(itemId, pantry).orElseThrow(() -> new ItemNotFoundException("The item was not found"));
    }
}
