package it.iacovelli.grocerybe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.model.dto.TransactionDto;
import it.iacovelli.grocerybe.model.response.BaseResponse;
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

    @Operation(summary = "Add a new item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item added") })
    @PostMapping("/")
    public BaseResponse<ItemDto> addItem(@RequestBody ItemDto itemDto) {
        ItemDto dto = itemService.addItem(itemDto);
        return new BaseResponse<>(dto, null);
    }

    @Operation(summary = "Get all items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items found")})
    @GetMapping("/")
    public BaseResponse<List<ItemDto>> getAllItems() {
        List<ItemDto> items = itemService.getAllItems();
        return new BaseResponse<>(items, null);
    }

    @Operation(summary = "Get an item by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item found")})
    @GetMapping("/{id}")
    public BaseResponse<ItemDto> getItem(@Parameter(description = "id of the item to be searched") @PathVariable UUID id) {
        ItemDto item = itemService.getItem(id);
        return new BaseResponse<>(item, null);
    }

    @Operation(summary = "Update an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated")})
    @PatchMapping("/{id}")
    public BaseResponse<ItemDto> updateItem(@Parameter(description = "id of the item to be updated") @PathVariable UUID id,
                                            @RequestBody ItemDto itemDto) {
        ItemDto dto = itemService.updateItem(id, itemDto);
        return new BaseResponse<>(dto, null);
    }

    @Operation(summary = "Delete an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item deleted",
                    content = { @Content(mediaType = "application/json") })})
    @DeleteMapping("/{id}")
    public BaseResponse<String> deleteItem(@Parameter(description = "id of the item to be deleted") @PathVariable UUID id) {
        itemService.deleteItem(id);
        return new BaseResponse<>("Item deleted", null);
    }

    @Operation(summary = "Get food detail by barcode")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food detail found")})
    @GetMapping("/{id}/detail")
    public BaseResponse<FoodDetailDto> getFoodDetail(@Parameter(description = "id of the item to be searched") @PathVariable UUID id) {
        FoodDetailDto foodDetail = itemService.getFoodDetail(id);
        return new BaseResponse<>(foodDetail, null);
    }

    @Operation(summary = "Add a new transaction to an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction added")})
    @PostMapping("/{id}/transaction")
    public BaseResponse<TransactionDto> addTransactionToItem(@Parameter(description = "id of the item to which the transaction belongs") @PathVariable("id") UUID itemId,
                                                             @RequestBody TransactionDto transactionDto) {
        transactionDto.setAvailableQuantity(transactionDto.getQuantity());
        TransactionDto dto = transactionService.addTransaction(transactionDto, itemId);
        return new BaseResponse<>(dto, null);
    }

    @Operation(summary = "Get all transactions of an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions found")})
    @GetMapping("/{id}/transaction")
    public BaseResponse<List<TransactionDto>> getItemTransactions(@Parameter(description = "id of the item from which get the transactions") @PathVariable("id") UUID itemId) {
        List<TransactionDto> itemTransactions = transactionService.getItemTransactions(itemId);
        return new BaseResponse<>(itemTransactions, null);
    }

    @Operation(summary = "Get Transaction detail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found")
    })
    @GetMapping("/{itemId}/transaction/{transactionId}")
    public BaseResponse<TransactionDto> getItemTransaction(@Parameter(description = "id of the item to which the transaction belongs") @PathVariable UUID itemId,
                                                    @Parameter(description = "id of the transaction searched") @PathVariable UUID transactionId) {
        TransactionDto itemTransaction = transactionService.getItemTransaction(itemId, transactionId);
        return new BaseResponse<>(itemTransaction, null);
    }

    @Operation(summary = "Update a transaction of an item by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated")})
    @PatchMapping("/{id}/transaction")
    public BaseResponse<TransactionDto> updateTransaction(@Parameter(description = "id of the item to which the transaction belongs") @PathVariable("id") UUID itemId,
                                                          @RequestBody TransactionDto transactionDto) {
        TransactionDto dto = transactionService.updateItemTransaction(itemId, transactionDto);
        return new BaseResponse<>(dto, null);
    }

    @Operation(summary = "Delete a transaction of an item by its id", description = "The transaction is deleted only if it belongs to the item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction deleted")})
    @DeleteMapping("/{itemId}/transaction/{transactionId}")
    public BaseResponse<String> deleteTransaction(@Parameter(description = "id of the item to which the transaction belongs") @PathVariable UUID itemId,
                                                  @Parameter(description = "id of the transaction to be deleted") @PathVariable UUID transactionId) {
        transactionService.deleteItemTransaction(itemId, transactionId);
        return new BaseResponse<>("Transaction deleted", null);
    }

}
