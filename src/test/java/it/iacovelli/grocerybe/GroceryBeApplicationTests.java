package it.iacovelli.grocerybe;

import it.iacovelli.grocerybe.exception.ItemBarcodeAlreadyExistsException;
import it.iacovelli.grocerybe.exception.ItemNotFoundException;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.model.dto.PantryDto;
import it.iacovelli.grocerybe.model.dto.TransactionDto;
import it.iacovelli.grocerybe.service.ItemService;
import it.iacovelli.grocerybe.service.PantryService;
import it.iacovelli.grocerybe.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class GroceryBeApplicationTests {

    private final String userId = "TEST-USER-ID";

    private final PantryDto pantryDto = new PantryDto(
            null,
            "PANTRY-TEST",
            "USER-TEST"
    );

    private final ItemDto itemDto = new ItemDto(
            null,
            null,
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

    @Autowired
    private PantryService pantryService;

    @Test
    void contextLoads() {
    }

    @Test
    void createPantry() {
        PantryDto savedPantryDto = pantryService.addPantry(pantryDto, userId);
        deletePantry(savedPantryDto.getId());
        assert savedPantryDto.getName().equals(pantryDto.getName());
    }

    @Test
    void addItem() {
        PantryDto savedPantryDto = pantryService.addPantry(pantryDto, userId);
        itemDto.setPantryId(savedPantryDto.getId());
        ItemDto savedItemDto = itemService.addItem(itemDto, userId);
        deleteItem(savedItemDto.getId());
        deletePantry(savedPantryDto.getId());
        assert savedItemDto.getName().equals(itemDto.getName()) && savedItemDto.getBarcode().equals(itemDto.getBarcode());
    }

    @Test()
    void throwItemAlreadyExists() {
        PantryDto savedPantryDto = pantryService.addPantry(pantryDto, userId);
        itemDto.setPantryId(savedPantryDto.getId());
        ItemDto savedItemDto = itemService.addItem(itemDto, userId);
        assertThrows(ItemBarcodeAlreadyExistsException.class, () -> itemService.addItem(itemDto, userId));
        deleteItem(savedItemDto.getId());
        deletePantry(savedPantryDto.getId());
    }

    @Test
    void getItemSaved() {
        PantryDto savedPantryDto = pantryService.addPantry(pantryDto, userId);
        itemDto.setPantryId(savedPantryDto.getId());
        ItemDto savedItemDto = itemService.addItem(itemDto, userId);
        ItemDto item = itemService.getItem(savedItemDto.getId(), userId, savedItemDto.getPantryId());
        assert item.equals(savedItemDto);
        deleteItem(savedItemDto.getId());
        deletePantry(savedPantryDto.getId());
    }

    @Test
    void getItemNotFoundException() {
        PantryDto savedPantryDto = pantryService.addPantry(pantryDto, userId);
        assertThrows(ItemNotFoundException.class, () -> itemService.getItem(UUID.randomUUID(), userId, savedPantryDto.getId()));
        deletePantry(savedPantryDto.getId());
    }

    @Test
    void getItemWithWrongUserId() {
        PantryDto savedPantryDto = pantryService.addPantry(pantryDto, userId);
        itemDto.setPantryId(savedPantryDto.getId());
        ItemDto savedItemDto = itemService.addItem(itemDto, userId);
        assertThrows(RuntimeException.class, () -> itemService.getItem(savedItemDto.getId(), "WRONG-USER-ID", savedItemDto.getPantryId()));
        deleteItem(savedItemDto.getId());
        deletePantry(savedPantryDto.getId());
    }

    @Test
    void getAllItems() {
        PantryDto savedPantryDto = pantryService.addPantry(pantryDto, userId);
        itemDto.setPantryId(savedPantryDto.getId());
        ItemDto savedItemDto = itemService.addItem(itemDto, userId);
        List<ItemDto> items = itemService.getAllItems(false, userId, itemDto.getPantryId());
        deleteItem(savedItemDto.getId());
        deletePantry(savedPantryDto.getId());
        assert items.size() == 1;
    }

    @Test
    void addTransactionToItem() {
        PantryDto savedPantryDto = pantryService.addPantry(pantryDto, userId);
        itemDto.setPantryId(savedPantryDto.getId());
        ItemDto savedItemDto = itemService.addItem(itemDto, userId);
        TransactionDto savedTransaction = transactionService.addTransaction(transactionDto, savedItemDto.getId(), itemDto.getPantryId());
        deleteTransaction(savedTransaction.getId(), savedItemDto.getId());
        deleteItem(savedItemDto.getId());
        deletePantry(savedPantryDto.getId());
        assert savedTransaction.getSeller().equals(transactionDto.getSeller()) && savedTransaction.getExpirationDate().equals(transactionDto.getExpirationDate());
    }

    @Test
    void getAllItemTransactions() {
        PantryDto savedPantryDto = pantryService.addPantry(pantryDto, userId);
        itemDto.setPantryId(savedPantryDto.getId());
        ItemDto savedItemDto = itemService.addItem(itemDto, userId);
        TransactionDto savedTransaction = transactionService.addTransaction(transactionDto, savedItemDto.getId(), itemDto.getPantryId());
        List<TransactionDto> itemTransactions = transactionService.getItemTransactions(savedItemDto.getId(), false, itemDto.getPantryId());
        deleteTransaction(savedTransaction.getId(), savedItemDto.getId());
        deleteItem(savedItemDto.getId());
        deletePantry(savedPantryDto.getId());
        assert itemTransactions.size() == 1;
    }

    @Test
    void updateTransaction() {
        PantryDto savedPantryDto = pantryService.addPantry(pantryDto, userId);
        itemDto.setPantryId(savedPantryDto.getId());
        ItemDto savedItemDto = itemService.addItem(itemDto, userId);
        TransactionDto savedTransaction = transactionService.addTransaction(transactionDto, savedItemDto.getId(), itemDto.getPantryId());
        savedTransaction.setSeller("VENDOR-UPDATED");
        TransactionDto updatedTransaction = transactionService.updateItemTransaction(savedItemDto.getId(), savedTransaction, itemDto.getPantryId());
        deleteTransaction(savedTransaction.getId(), savedItemDto.getId());
        deleteItem(savedItemDto.getId());
        assert updatedTransaction.getSeller().equals(savedTransaction.getSeller()) && updatedTransaction.getExpirationDate().equals(savedTransaction.getExpirationDate());
    }

    private void deleteItem(UUID itemId) {
        itemService.deleteItem(itemId, userId, itemDto.getPantryId());
    }

    private void deleteTransaction(UUID transactionId, UUID itemId) {
        transactionService.deleteItemTransaction(itemId, transactionId, itemDto.getPantryId());
    }

    private void deletePantry(UUID pantryId) {
        pantryService.deletePantry(pantryId, userId);
    }

}
