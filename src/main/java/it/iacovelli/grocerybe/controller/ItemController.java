package it.iacovelli.grocerybe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.model.dto.ItemStatisticDto;
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
public class ItemController extends BaseController {

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
    public BaseResponse<List<ItemDto>> getAllItems(
            @Parameter(description = "flag for getting only available items") @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
            @RequestHeader("iv-user") String userId) {
        List<ItemDto> items = itemService.getAllItems(onlyAvailable, userId);
        return new BaseResponse<>(items, null);
    }

    @Operation(summary = "Get items statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items statistics found")})
    @GetMapping("/statistics")
    public BaseResponse<ItemStatisticDto> getStatistics(@RequestHeader("iv-user") String userId) {
        ItemStatisticDto itemsStatistic = itemService.getItemsStatistic(userId);
        return new BaseResponse<>(itemsStatistic, null);
    }

    @Operation(summary = "Get an item by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item found")})
    @GetMapping("/{id}")
    public BaseResponse<ItemDto> getItem(@Parameter(description = "id of the item to be searched") @PathVariable UUID id,
                                         @RequestHeader("iv-user") String userId) {
        ItemDto item = itemService.getItem(id, userId);
        return new BaseResponse<>(item, null);
    }

    @Operation(summary = "Update an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated")})
    @PatchMapping("/{id}")
    public BaseResponse<ItemDto> updateItem(@Parameter(description = "id of the item to be updated") @PathVariable UUID id,
                                            @RequestBody ItemDto itemDto,
                                            @RequestHeader("iv-user") String userId) {
        ItemDto dto = itemService.updateItem(id, itemDto, userId);
        return new BaseResponse<>(dto, null);
    }

    @Operation(summary = "Delete an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item deleted",
                    content = { @Content(mediaType = "application/json") })})
    @DeleteMapping("/{id}")
    public BaseResponse<String> deleteItem(@Parameter(description = "id of the item to be deleted") @PathVariable UUID id,
                                           @RequestHeader("iv-user") String userId) {
        itemService.deleteItem(id, userId);
        return new BaseResponse<>("Item deleted", null);
    }

    @Operation(summary = "Get food detail by barcode")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food detail found")})
    @GetMapping("/{id}/detail")
    public BaseResponse<FoodDetailDto> getFoodDetail(@Parameter(description = "id of the item to be searched") @PathVariable UUID id,
                                                     @RequestHeader("iv-user") String userId) {
        FoodDetailDto foodDetail = itemService.getFoodDetail(id, userId);
        return new BaseResponse<>(foodDetail, null);
    }

    @Operation(summary = "Get kcal consumed for item and quantity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kcal consumed found")})
    @GetMapping("/{id}/kcal")
    public BaseResponse<Float> getKcalConsumedForQuantity(@Parameter(description = "id of the item to be searched") @PathVariable UUID id,
                                                          @Parameter(description = "quantity of the item to be searched") @RequestParam float quantity,
                                                          @RequestHeader("iv-user") String userId) {
        float kcal = itemService.getKcalConsumedForItemAndQuantity(id, quantity, userId);
        return new BaseResponse<>(kcal, null);
    }

    @Operation(summary = "Add a new transaction to an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction added")})
    @PostMapping("/{id}/transaction")
    public BaseResponse<TransactionDto> addTransactionToItem(@Parameter(description = "id of the item to which the transaction belongs") @PathVariable("id") UUID itemId,
                                                             @RequestBody TransactionDto transactionDto,
                                                             @RequestHeader("iv-user") String userId) {
        transactionDto.setAvailableQuantity(transactionDto.getQuantity());
        TransactionDto dto = transactionService.addTransaction(transactionDto, itemId, userId);
        return new BaseResponse<>(dto, null);
    }

    @Operation(summary = "Get all transactions of an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions found")})
    @GetMapping("/{id}/transaction")
    public BaseResponse<List<TransactionDto>> getItemTransactions(
            @Parameter(description = "id of the item from which get the transactions") @PathVariable("id") UUID itemId,
            @Parameter(description = "flag for getting only available transaction") @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
            @RequestHeader("iv-user") String userId) {
        List<TransactionDto> itemTransactions = transactionService.getItemTransactions(itemId, onlyAvailable, userId);
        return new BaseResponse<>(itemTransactions, null);
    }

    @Operation(summary = "Get Transaction detail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found")
    })
    @GetMapping("/{itemId}/transaction/{transactionId}")
    public BaseResponse<TransactionDto> getItemTransaction(@Parameter(description = "id of the item to which the transaction belongs") @PathVariable UUID itemId,
                                                           @Parameter(description = "id of the transaction searched") @PathVariable UUID transactionId,
                                                           @RequestHeader("iv-user") String userId) {
        TransactionDto itemTransaction = transactionService.getItemTransaction(itemId, transactionId, userId);
        return new BaseResponse<>(itemTransaction, null);
    }

    @Operation(summary = "Update a transaction of an item by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated")})
    @PatchMapping("/{id}/transaction")
    public BaseResponse<TransactionDto> updateTransaction(@Parameter(description = "id of the item to which the transaction belongs") @PathVariable("id") UUID itemId,
                                                          @RequestBody TransactionDto transactionDto,
                                                          @RequestHeader("iv-user") String userId) {
        TransactionDto dto = transactionService.updateItemTransaction(itemId, transactionDto, userId);
        return new BaseResponse<>(dto, null);
    }

    @Operation(summary = "Delete a transaction of an item by its id", description = "The transaction is deleted only if it belongs to the item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction deleted")})
    @DeleteMapping("/{itemId}/transaction/{transactionId}")
    public BaseResponse<String> deleteTransaction(@Parameter(description = "id of the item to which the transaction belongs") @PathVariable UUID itemId,
                                                  @Parameter(description = "id of the transaction to be deleted") @PathVariable UUID transactionId,
                                                  @RequestHeader("iv-user") String userId) {
        transactionService.deleteItemTransaction(itemId, transactionId, userId);
        return new BaseResponse<>("Transaction deleted", null);
    }

}
