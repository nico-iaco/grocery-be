package it.iacovelli.grocerybe.service.impl;

import it.iacovelli.grocerybe.exception.ItemBarcodeAlreadyExistsException;
import it.iacovelli.grocerybe.exception.ItemNotFoundException;
import it.iacovelli.grocerybe.mapper.ItemMapper;
import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.repository.ItemRepository;
import it.iacovelli.grocerybe.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    @Override
    public ItemDto addItem(ItemDto itemDto) throws ItemBarcodeAlreadyExistsException {
        if (itemRepository.countItemsByBarcode(itemDto.getBarcode()) > 0) {
            throw new ItemBarcodeAlreadyExistsException("Barcode already exists, check if the information is correct");
        }
        Item item = itemMapper.dtoToEntity(itemDto);
        return itemMapper.entityToDto(itemRepository.save(item));
    }

    @Override
    public List<ItemDto> getAllItems() {
        List<ItemDto> itemsDtoList = new ArrayList<>();
        itemRepository.findAll().forEach(item -> itemsDtoList.add(itemMapper.entityToDto(item)));
        return itemsDtoList;
    }

    @Override
    public ItemDto getItem(UUID id) throws ItemNotFoundException {
        Item item = itemRepository.findItemById(id).orElseThrow(() -> new ItemNotFoundException("The item was not found"));
        return itemMapper.entityToDto(item);
    }

    @Override
    public ItemDto updateItem(UUID id, ItemDto itemDto) throws ItemNotFoundException {
        Item item = itemRepository.findItemById(id).orElseThrow(() -> new ItemNotFoundException("The item was not found"));
        itemMapper.updateItem(itemDto, item);
        return itemMapper.entityToDto(itemRepository.save(item));
    }

    @Override
    public void deleteItem(UUID id) throws ItemNotFoundException {
        Item item = itemRepository.findItemById(id).orElseThrow(() -> new ItemNotFoundException("The item was not found"));
        itemRepository.delete(item);
    }
}
