package it.iacovelli.grocerybe.service;

import it.iacovelli.grocerybe.exception.FoodDetailsNotAvailableException;
import it.iacovelli.grocerybe.exception.ItemBarcodeAlreadyExistsException;
import it.iacovelli.grocerybe.exception.ItemNotFoundException;
import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.model.dto.ItemStatisticWrapperDto;
import it.iacovelli.grocerybe.model.dto.ShoppingItemDto;

import java.util.List;
import java.util.UUID;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto) throws ItemBarcodeAlreadyExistsException;

    void addAllItems(List<ShoppingItemDto> shoppingItemList) throws ItemBarcodeAlreadyExistsException;

    List<ItemDto> getAllItems(boolean onlyAvailable, String userId);

    ItemDto getItem(UUID id, String userid) throws ItemNotFoundException;

    ItemDto updateItem(UUID id, ItemDto itemDto, String userid) throws ItemNotFoundException;

    void deleteItem(UUID id, String userid) throws ItemNotFoundException;

    FoodDetailDto getFoodDetail(UUID itemId, String userid) throws ItemNotFoundException, FoodDetailsNotAvailableException;

    float getKcalConsumedForItemAndQuantity(UUID itemId, float quantity, String userid) throws ItemNotFoundException;

    ItemStatisticWrapperDto getItemsStatistic(String userid);

}
