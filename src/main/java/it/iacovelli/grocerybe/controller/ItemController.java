package it.iacovelli.grocerybe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.model.dto.ItemStatisticWrapperDto;
import it.iacovelli.grocerybe.model.dto.TransactionDto;
import it.iacovelli.grocerybe.model.request.ShoppingCartRequest;
import it.iacovelli.grocerybe.model.response.BaseResponse;
import it.iacovelli.grocerybe.service.ItemService;
import it.iacovelli.grocerybe.service.TransactionService;
import it.iacovelli.grocerybe.utils.FirebaseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController extends BaseController {

    private final ItemService itemService;

    private final TransactionService transactionService;

    private final FirebaseUtils firebaseUtils;

    @Operation(summary = "Add a new item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item added")})
    @PostMapping("/")
    public BaseResponse<ItemDto> addItem(
            @RequestHeader("Authorization") String token,
            @RequestParam UUID pantryId,
            @RequestBody ItemDto itemDto) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        itemDto.setPantryId(pantryId);
        ItemDto dto = itemService.addItem(itemDto, userId);
        return new BaseResponse<>(dto, null);
    }

    @Operation(summary = "Add all items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items added")})
    @PostMapping("/all")
    public BaseResponse<Void> addItems(
            @RequestHeader("Authorization") String token,
            @RequestParam UUID pantryId,
            @RequestBody ShoppingCartRequest shoppingCartRequest) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        itemService.addAllItems(shoppingCartRequest.getShoppingItems(), userId, pantryId);
        return new BaseResponse<>(null, null);
    }

    @Operation(summary = "Get all items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items found")})
    @GetMapping("/")
    public BaseResponse<List<ItemDto>> getAllItems(
            @Parameter(description = "flag for getting only available items") @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
            @RequestParam UUID pantryId,
            @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        List<ItemDto> items = itemService.getAllItems(onlyAvailable, userId, pantryId);
        return new BaseResponse<>(items, null);
    }

    @Operation(summary = "Get items statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items statistics found")})
    @GetMapping("/statistics")
    public BaseResponse<ItemStatisticWrapperDto> getStatistics(
            @RequestHeader("Authorization") String token,
            @RequestParam UUID pantryId) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        ItemStatisticWrapperDto itemsStatistic = itemService.getItemsStatistic(userId, pantryId);
        return new BaseResponse<>(itemsStatistic, null);
    }

    @Operation(summary = "Get an item by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item found")})
    @GetMapping("/{id}")
    public BaseResponse<ItemDto> getItem(@Parameter(description = "id of the item to be searched") @PathVariable UUID id,
                                         @RequestParam UUID pantryId,
                                         @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        ItemDto item = itemService.getItem(id, userId, pantryId);
        return new BaseResponse<>(item, null);
    }

    @Operation(summary = "Update an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated")})
    @PatchMapping("/{id}")
    public BaseResponse<ItemDto> updateItem(@Parameter(description = "id of the item to be updated") @PathVariable UUID id,
                                            @RequestBody ItemDto itemDto,
                                            @RequestParam UUID pantryId,
                                            @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        ItemDto dto = itemService.updateItem(id, itemDto, userId, pantryId);
        return new BaseResponse<>(dto, null);
    }

    @Operation(summary = "Delete an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item deleted",
                    content = {@Content(mediaType = "application/json")})})
    @DeleteMapping("/{id}")
    public BaseResponse<String> deleteItem(@Parameter(description = "id of the item to be deleted") @PathVariable UUID id,
                                           @RequestParam UUID pantryId,
                                           @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        itemService.deleteItem(id, userId, pantryId);
        return new BaseResponse<>("Item deleted", null);
    }

    @Operation(summary = "Get food detail by barcode")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food detail found")})
    @GetMapping("/{id}/detail")
    public BaseResponse<FoodDetailDto> getFoodDetail(@Parameter(description = "id of the item to be searched") @PathVariable UUID id,
                                                     @RequestParam UUID pantryId,
                                                     @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        FoodDetailDto foodDetail = itemService.getFoodDetail(id, userId, pantryId);
        return new BaseResponse<>(foodDetail, null);
    }

    @Operation(summary = "Get kcal consumed for item and quantity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kcal consumed found")})
    @GetMapping("/{id}/kcal")
    public BaseResponse<Float> getKcalConsumedForQuantity(@Parameter(description = "id of the item to be searched") @PathVariable UUID id,
                                                          @Parameter(description = "quantity of the item to be searched") @RequestParam float quantity,
                                                          @RequestParam UUID pantryId,
                                                          @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        float kcal = itemService.getKcalConsumedForItemAndQuantity(id, quantity, userId, pantryId);
        return new BaseResponse<>(kcal, null);
    }

    @Operation(summary = "Add a new transaction to an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction added")})
    @PostMapping("/{id}/transaction")
    public BaseResponse<TransactionDto> addTransactionToItem(@Parameter(description = "id of the item to which the transaction belongs") @PathVariable("id") UUID itemId,
                                                             @RequestBody TransactionDto transactionDto,
                                                             @RequestParam UUID pantryId,
                                                             @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        transactionDto.setAvailableQuantity(transactionDto.getQuantity());
        TransactionDto dto = transactionService.addTransaction(transactionDto, itemId, pantryId);
        return new BaseResponse<>(dto, null);
    }

    @Operation(summary = "Get all transactions of an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions found")})
    @GetMapping("/{id}/transaction")
    public BaseResponse<List<TransactionDto>> getItemTransactions(
            @Parameter(description = "id of the item from which get the transactions") @PathVariable("id") UUID itemId,
            @Parameter(description = "flag for getting only available transaction") @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
            @RequestParam UUID pantryId,
            @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        List<TransactionDto> itemTransactions = transactionService.getItemTransactions(itemId, onlyAvailable, pantryId);
        return new BaseResponse<>(itemTransactions, null);
    }

    @Operation(summary = "Get Transaction detail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found")
    })
    @GetMapping("/{itemId}/transaction/{transactionId}")
    public BaseResponse<TransactionDto> getItemTransaction(@Parameter(description = "id of the item to which the transaction belongs") @PathVariable UUID itemId,
                                                           @Parameter(description = "id of the transaction searched") @PathVariable UUID transactionId,
                                                           @RequestParam UUID pantryId,
                                                           @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        TransactionDto itemTransaction = transactionService.getItemTransaction(itemId, transactionId, pantryId);
        return new BaseResponse<>(itemTransaction, null);
    }

    @Operation(summary = "Update a transaction of an item by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated")})
    @PatchMapping("/{id}/transaction")
    public BaseResponse<TransactionDto> updateTransaction(@Parameter(description = "id of the item to which the transaction belongs") @PathVariable("id") UUID itemId,
                                                          @RequestBody TransactionDto transactionDto,
                                                          @RequestParam UUID pantryId,
                                                          @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        TransactionDto dto = transactionService.updateItemTransaction(itemId, transactionDto, pantryId);
        return new BaseResponse<>(dto, null);
    }

    @Operation(summary = "Delete a transaction of an item by its id", description = "The transaction is deleted only if it belongs to the item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction deleted")})
    @DeleteMapping("/{itemId}/transaction/{transactionId}")
    public BaseResponse<String> deleteTransaction(@Parameter(description = "id of the item to which the transaction belongs") @PathVariable UUID itemId,
                                                  @Parameter(description = "id of the transaction to be deleted") @PathVariable UUID transactionId,
                                                  @RequestParam UUID pantryId,
                                                  @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        transactionService.deleteItemTransaction(itemId, transactionId, pantryId);
        return new BaseResponse<>("Transaction deleted", null);
    }

}
