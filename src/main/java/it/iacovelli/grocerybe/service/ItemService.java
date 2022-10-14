package it.iacovelli.grocerybe.service;

import it.iacovelli.grocerybe.exception.FoodDetailsNotAvailableException;
import it.iacovelli.grocerybe.exception.ItemBarcodeAlreadyExistsException;
import it.iacovelli.grocerybe.exception.ItemNotFoundException;
import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.model.dto.ItemStatisticDto;

import java.util.List;
import java.util.UUID;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto) throws ItemBarcodeAlreadyExistsException;

    List<ItemDto> getAllItems(boolean onlyAvailable);

    ItemDto getItem(UUID id) throws ItemNotFoundException;

    ItemDto updateItem(UUID id, ItemDto itemDto) throws ItemNotFoundException;

    void deleteItem(UUID id) throws ItemNotFoundException;

    FoodDetailDto getFoodDetail(UUID itemId) throws ItemNotFoundException, FoodDetailsNotAvailableException;

    float getKcalConsumedForItemAndQuantity(UUID itemId, float quantity) throws ItemNotFoundException;

    ItemStatisticDto getItemsStatistic();

}
