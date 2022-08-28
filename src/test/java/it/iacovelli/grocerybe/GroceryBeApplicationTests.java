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
            "BARCODE-TEST",
            "TEST",
            0,
            ""
    );

    private final TransactionDto transactionDto = new TransactionDto(
            null,
            "VENDOR",
            20.0,
            "unit",
            43.2,
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
        ItemDto item = itemService.getItem(savedItemDto.getId());
        deleteItem(savedItemDto.getId());
        assert item.equals(savedItemDto);
    }

    @Test
    void getAllItems() {
        ItemDto savedItemDto = itemService.addItem(itemDto);
        List<ItemDto> items = itemService.getAllItems();
        deleteItem(savedItemDto.getId());
        assert items.size() == 1;
    }

    @Test
    void addTransactionToItem() {
        ItemDto savedItemDto = itemService.addItem(itemDto);
        TransactionDto savedTransaction = transactionService.addTransaction(transactionDto, savedItemDto.getId());
        deleteTransaction(savedTransaction.getId(), savedItemDto.getId());
        deleteItem(savedItemDto.getId());
        assert savedTransaction.getVendor().equals(transactionDto.getVendor()) && savedTransaction.getExpirationDate().equals(transactionDto.getExpirationDate());
    }

    @Test
    void getAllItemTransactions() {
        ItemDto savedItemDto = itemService.addItem(itemDto);
        TransactionDto savedTransaction = transactionService.addTransaction(transactionDto, savedItemDto.getId());
        List<TransactionDto> itemTransactions = transactionService.getItemTransactions(savedItemDto.getId());
        deleteTransaction(savedTransaction.getId(), savedItemDto.getId());
        deleteItem(savedItemDto.getId());
        assert itemTransactions.size() == 1;
    }

    private void deleteItem(UUID itemId) {
        itemService.deleteItem(itemId);
    }

    private void deleteTransaction(UUID transactionId, UUID itemId) {
        transactionService.deleteItemTransaction(itemId, transactionId);
    }

}
