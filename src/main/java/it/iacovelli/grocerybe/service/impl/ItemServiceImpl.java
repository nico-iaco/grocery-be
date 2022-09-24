package it.iacovelli.grocerybe.service.impl;

import it.iacovelli.grocerybe.exception.ItemBarcodeAlreadyExistsException;
import it.iacovelli.grocerybe.exception.ItemNotFoundException;
import it.iacovelli.grocerybe.mapper.ItemMapper;
import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.repository.ItemRepository;
import it.iacovelli.grocerybe.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private final RestTemplate restTemplate;

    @Value("${grocery-be.external.food-details-integrator-be.details-path}")
    private String foodDetailsEndpoint;

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

    @Override
    public FoodDetailDto getFoodDetail(UUID itemId) throws ItemNotFoundException {
        Item item = itemRepository.findItemById(itemId).orElseThrow(() -> new ItemNotFoundException("The item was not found"));
        FoodDetailDto detailDto = restTemplate.getForObject(foodDetailsEndpoint, FoodDetailDto.class, item.getBarcode());
        return detailDto;
    }
}
