package it.iacovelli.grocerybe.service;

import it.iacovelli.grocerybe.exception.ItemBarcodeAlreadyExistsException;
import it.iacovelli.grocerybe.exception.ItemNotFoundException;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto) throws ItemBarcodeAlreadyExistsException;

    List<ItemDto> getAllItems();

    ItemDto getItem(UUID id) throws ItemNotFoundException;

    ItemDto updateItem(UUID id, ItemDto itemDto) throws ItemNotFoundException;

    void deleteItem(UUID id) throws ItemNotFoundException;

}
