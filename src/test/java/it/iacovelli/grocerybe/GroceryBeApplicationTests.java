package it.iacovelli.grocerybe;

import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.model.dto.TransactionDto;
import it.iacovelli.grocerybe.service.ItemService;
import it.iacovelli.grocerybe.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class GroceryBeApplicationTests {

    private final ItemDto itemDto = new ItemDto(
            null,
            "TEST-USER-ID",
            "VENDOR-TEST",
            "BARCODE-TEST",
            "TEST",
            0,
            0,
            "g",
            LocalDate.now()
    );

    private final TransactionDto transactionDto = new TransactionDto(
            null,
            "VENDOR",
            20.0,
            20.0,
            20.0,
            "unit",
            43.2,
            LocalDate.of(2022, 7, 6),
            LocalDate.of(2022, 7, 6)
    );

    @Autowired
    private ItemService itemService;

    @Autowired
    private TransactionService transactionService;

    @Test
    void contextLoads() {
    }

    @Test
    void addItem() {
        ItemDto savedItemDto = itemService.addItem(itemDto);
        deleteItem(savedItemDto.getId());
        assert savedItemDto.getName().equals(itemDto.getName()) && savedItemDto.getBarcode().equals(itemDto.getBarcode());
    }

    @Test
    void getItemSaved() {
        ItemDto savedItemDto = itemService.addItem(itemDto);
        ItemDto item = itemService.getItem(savedItemDto.getId(), savedItemDto.getUserId());
        deleteItem(savedItemDto.getId());
        assert item.equals(savedItemDto);
    }

    @Test
    void getAllItems() {
        ItemDto savedItemDto = itemService.addItem(itemDto);
        List<ItemDto> items = itemService.getAllItems(false, itemDto.getUserId());
        deleteItem(savedItemDto.getId());
        assert items.size() == 1;
    }

    @Test
    void addTransactionToItem() {
        ItemDto savedItemDto = itemService.addItem(itemDto);
        TransactionDto savedTransaction = transactionService.addTransaction(transactionDto, savedItemDto.getId(), itemDto.getUserId());
        deleteTransaction(savedTransaction.getId(), savedItemDto.getId());
        deleteItem(savedItemDto.getId());
        assert savedTransaction.getSeller().equals(transactionDto.getSeller()) && savedTransaction.getExpirationDate().equals(transactionDto.getExpirationDate());
    }

    @Test
    void getAllItemTransactions() {
        ItemDto savedItemDto = itemService.addItem(itemDto);
        TransactionDto savedTransaction = transactionService.addTransaction(transactionDto, savedItemDto.getId(), itemDto.getUserId());
        List<TransactionDto> itemTransactions = transactionService.getItemTransactions(savedItemDto.getId(), false, itemDto.getUserId());
        deleteTransaction(savedTransaction.getId(), savedItemDto.getId());
        deleteItem(savedItemDto.getId());
        assert itemTransactions.size() == 1;
    }

    @Test
    void updateTransaction() {
        ItemDto savedItemDto = itemService.addItem(itemDto);
        TransactionDto savedTransaction = transactionService.addTransaction(transactionDto, savedItemDto.getId(), itemDto.getUserId());
        savedTransaction.setSeller("VENDOR-UPDATED");
        TransactionDto updatedTransaction = transactionService.updateItemTransaction(savedItemDto.getId(), savedTransaction, itemDto.getUserId());
        deleteTransaction(savedTransaction.getId(), savedItemDto.getId());
        deleteItem(savedItemDto.getId());
        assert updatedTransaction.getSeller().equals(savedTransaction.getSeller()) && updatedTransaction.getExpirationDate().equals(savedTransaction.getExpirationDate());
    }

    private void deleteItem(UUID itemId) {
        itemService.deleteItem(itemId, itemDto.getUserId());
    }

    private void deleteTransaction(UUID transactionId, UUID itemId) {
        transactionService.deleteItemTransaction(itemId, transactionId, itemDto.getUserId());
    }

}
