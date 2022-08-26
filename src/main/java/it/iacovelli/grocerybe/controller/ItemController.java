package it.iacovelli.grocerybe.controller;

import it.iacovelli.grocerybe.model.dto.TransactionDto;
import it.iacovelli.grocerybe.model.response.BaseResponse;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.service.ItemService;
import it.iacovelli.grocerybe.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private final TransactionService transactionService;

    @PostMapping("/")
    public BaseResponse<ItemDto> addItem(@RequestBody ItemDto itemDto) {
        ItemDto dto = itemService.addItem(itemDto);
        return new BaseResponse<>(dto, null);
    }

    @GetMapping("/")
    public BaseResponse<List<ItemDto>> getAllItems() {
        List<ItemDto> items = itemService.getAllItems();
        return new BaseResponse<>(items, null);
    }

    @GetMapping("/{id}")
    public BaseResponse<ItemDto> getItem(@PathVariable UUID id) {
        ItemDto item = itemService.getItem(id);
        return new BaseResponse<>(item, null);
    }

    @PatchMapping("/{id}")
    public BaseResponse<ItemDto> updateItem(@PathVariable UUID id, @RequestBody ItemDto itemDto) {
        ItemDto dto = itemService.updateItem(id, itemDto);
        return new BaseResponse<>(dto, null);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<String> deleteItem(@PathVariable UUID id) {
        itemService.deleteItem(id);
        return new BaseResponse<>("Item deleted", null);
    }

    @PostMapping("/{id}/transaction")
    public BaseResponse<TransactionDto> addTransactionToItem(@PathVariable("id") UUID itemId, @RequestBody TransactionDto transactionDto) {
        TransactionDto dto = transactionService.addTransaction(transactionDto, itemId);
        return new BaseResponse<>(dto, null);
    }

    @GetMapping("/{id}/transaction")
    public BaseResponse<List<TransactionDto>> getItemTransactions(@PathVariable("id") UUID itemId) {
        List<TransactionDto> itemTransactions = transactionService.getItemTransactions(itemId);
        return new BaseResponse<>(itemTransactions, null);
    }

    @PatchMapping("/{id}/transaction")
    public BaseResponse<TransactionDto> updateTransaction(@PathVariable("id") UUID itemId, @RequestBody TransactionDto transactionDto) {
        TransactionDto dto = transactionService.updateItemTransaction(itemId, transactionDto);
        return new BaseResponse<>(dto, null);
    }

    @DeleteMapping("/{itemId}/transaction/{transactionId}")
    public BaseResponse<String> deleteTransaction(@PathVariable UUID itemId, @PathVariable UUID transactionId) {
        transactionService.deleteItemTransaction(itemId, transactionId);
        return new BaseResponse<>("Transaction deleted", null);
    }

}
