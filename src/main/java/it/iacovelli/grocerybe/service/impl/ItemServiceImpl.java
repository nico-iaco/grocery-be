package it.iacovelli.grocerybe.service.impl;

import it.iacovelli.grocerybe.exception.ItemBarcodeAlreadyExistsException;
import it.iacovelli.grocerybe.exception.ItemNotFoundException;
import it.iacovelli.grocerybe.mapper.ItemMapper;
import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.model.dto.ItemStatisticDto;
import it.iacovelli.grocerybe.repository.ItemRepository;
import it.iacovelli.grocerybe.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
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

    @Value("${grocery-be.external.food-details-integrator-be.kcal-consumed-path}")
    private String kcalConsumedEndpoint;

    @Override
    public ItemDto addItem(ItemDto itemDto) throws ItemBarcodeAlreadyExistsException {
        if (itemRepository.countItemsByBarcode(itemDto.getBarcode()) > 0) {
            throw new ItemBarcodeAlreadyExistsException("Barcode already exists, check if the information is correct");
        }
        Item item = itemMapper.dtoToEntity(itemDto);
        return itemMapper.entityToDto(itemRepository.save(item));
    }

    @Override
    public List<ItemDto> getAllItems(boolean onlyAvailable) {
        List<ItemDto> itemsDtoList = new ArrayList<>();
        itemRepository.findAll().forEach(item -> itemsDtoList.add(itemMapper.entityToDto(item)));
        if (onlyAvailable) {
            itemsDtoList.removeIf(itemDto -> itemDto.getAvailableQuantity() == 0);
        }
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

    @Override
    public float getKcalConsumedForItemAndQuantity(UUID itemId, float quantity) throws ItemNotFoundException {
        Item item = itemRepository.findItemById(itemId).orElseThrow(() -> new ItemNotFoundException("The item was not found"));
        Float kcalConsumed = restTemplate.getForObject(kcalConsumedEndpoint, Float.class, item.getBarcode(), quantity);
        return kcalConsumed != null ? kcalConsumed : 0;
    }

    @Override
    public ItemStatisticDto getItemsStatistic() {
        ItemStatisticDto itemStatisticDto = new ItemStatisticDto();
        LocalDate nowPlusOneWeek = LocalDate.now().plusWeeks(1);
        List<ItemDto> itemsInExpiration = itemRepository.findItemsInExpiration(nowPlusOneWeek).stream().map(itemMapper::entityToDto).toList();
        itemStatisticDto.setItemsInExpiration(itemsInExpiration);
        List<ItemDto> itemsAlmostFinished = itemRepository.findItemsAlmostFinished().stream().map(itemMapper::entityToDto).toList();
        itemStatisticDto.setItemsAlmostFinished(itemsAlmostFinished);
        return itemStatisticDto;
    }
}
