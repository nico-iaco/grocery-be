package it.iacovelli.grocerybe.service.impl;

import it.iacovelli.grocerybe.exception.FoodDetailsNotAvailableException;
import it.iacovelli.grocerybe.exception.ItemBarcodeAlreadyExistsException;
import it.iacovelli.grocerybe.exception.ItemNotFoundException;
import it.iacovelli.grocerybe.exception.PantryNotFoundException;
import it.iacovelli.grocerybe.mapper.FoodDetailMapper;
import it.iacovelli.grocerybe.mapper.ItemMapper;
import it.iacovelli.grocerybe.model.FoodDetail;
import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.Pantry;
import it.iacovelli.grocerybe.model.dto.*;
import it.iacovelli.grocerybe.repository.FoodDetailRepository;
import it.iacovelli.grocerybe.repository.ItemRepository;
import it.iacovelli.grocerybe.service.FoodDetailsIntegratorService;
import it.iacovelli.grocerybe.service.ItemService;
import it.iacovelli.grocerybe.service.PantryService;
import it.iacovelli.grocerybe.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final FoodDetailRepository foodDetailRepository;

    private final TransactionService transactionService;

    private final ItemMapper itemMapper;

    private final FoodDetailMapper foodDetailMapper;

    private final FoodDetailsIntegratorService foodDetailsIntegratorService;

    private final PantryService pantryService;

    private static final Log LOGGER = LogFactory.getLog(ItemServiceImpl.class);


    @Override
    public ItemDto addItem(ItemDto itemDto, String userId) throws ItemBarcodeAlreadyExistsException {

        Optional<Pantry> optionalPantry = pantryService.getPantryById(userId, itemDto.getPantryId());

        Pantry pantry = optionalPantry.orElseThrow(() -> new PantryNotFoundException("Pantry not found"));
        if (itemRepository.countItemsByBarcodeAndPantry(itemDto.getBarcode(), pantry) > 0) {
            throw new ItemBarcodeAlreadyExistsException("Barcode already exists, check if the information is correct");
        }
        Item item = itemMapper.dtoToEntity(itemDto);
        item.setPantry(pantry);
        return itemMapper.entityToDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public void addAllItems(List<ShoppingItemDto> shoppingItemList, String userid, UUID pantryId) throws ItemBarcodeAlreadyExistsException {
        for (ShoppingItemDto shoppingItemDto : shoppingItemList) {
            ItemDto itemDto = shoppingItemDto.getItem();
            TransactionDto transactionDto = shoppingItemDto.getTransaction();
            ItemDto savedItemDto = addItem(itemDto, userid);
            transactionService.addTransaction(transactionDto, savedItemDto.getId(), savedItemDto.getPantryId());
        }
    }

    @Override
    public List<ItemDto> getAllItems(boolean onlyAvailable, String userId, UUID pantryId) {
        Optional<Pantry> optionalPantry = pantryService.getPantryById(userId, pantryId);
        Pantry pantry = optionalPantry.orElseThrow(() -> new PantryNotFoundException("Pantry not found"));
        return itemRepository.findAllByPantry(pantry)
                .stream()
                .map(itemMapper::entityToDto)
                .filter(item -> !onlyAvailable || item.getAvailableQuantity() > 0)
                .toList();
    }

    @Override
    public ItemDto getItem(UUID id, String userid, UUID pantryId) throws ItemNotFoundException {
        Optional<Pantry> optionalPantry = pantryService.getPantryById(userid, pantryId);
        Pantry pantry = optionalPantry.orElseThrow(() -> new PantryNotFoundException("Pantry not found"));
        Item item = itemRepository.findItemByIdAndPantry(id, pantry).orElseThrow(() -> new ItemNotFoundException("The item was not found"));
        return itemMapper.entityToDto(item);
    }

    @Override
    public ItemDto updateItem(UUID id, ItemDto itemDto, String userid, UUID pantryId) throws ItemNotFoundException {
        Optional<Pantry> optionalPantry = pantryService.getPantryById(userid, pantryId);
        Pantry pantry = optionalPantry.orElseThrow(() -> new PantryNotFoundException("Pantry not found"));
        Item item = itemRepository.findItemByIdAndPantry(id, pantry).orElseThrow(() -> new ItemNotFoundException("The item was not found"));
        itemMapper.updateItem(itemDto, item);
        return itemMapper.entityToDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public void deleteItem(UUID id, String userid, UUID pantryId) throws ItemNotFoundException {
        Optional<Pantry> optionalPantry = pantryService.getPantryById(userid, pantryId);
        Pantry pantry = optionalPantry.orElseThrow(() -> new PantryNotFoundException("Pantry not found"));
        Item item = itemRepository.findItemByIdAndPantry(id, pantry).orElseThrow(() -> new ItemNotFoundException("The item was not found"));
        if (item.getFoodDetail() != null) {
            foodDetailRepository.delete(item.getFoodDetail());
        }
        if (!item.getTransactionList().isEmpty()) {
            item.getTransactionList()
                    .forEach(transaction -> transactionService.deleteItemTransaction(item.getId(), transaction.getId(), pantryId));
        }
        itemRepository.delete(item);
    }

    @Override
    public FoodDetailDto getFoodDetail(UUID itemId, String userid, UUID pantryId) throws ItemNotFoundException, FoodDetailsNotAvailableException {
        Optional<Pantry> optionalPantry = pantryService.getPantryById(userid, pantryId);
        Pantry pantry = optionalPantry.orElseThrow(() -> new PantryNotFoundException("Pantry not found"));
        Item item = itemRepository.findItemByIdAndPantry(itemId, pantry).orElseThrow(() -> new ItemNotFoundException("The item was not found"));

        Optional<FoodDetail> optionalFoodDetail = foodDetailRepository.findFoodDetailByItem(item);

        if (optionalFoodDetail.isPresent()) {
            LOGGER.info("Food details found in DB");
            return foodDetailMapper.entityToDto(optionalFoodDetail.get());
        } else {
            FoodDetailDto foodDetailDto = foodDetailsIntegratorService.getFoodDetails(item.getBarcode());
            if (foodDetailDto != null) {
                saveFoodDetail(foodDetailDto, item);
                return foodDetailDto;
            } else {
                throw new FoodDetailsNotAvailableException("Food details not available");
            }
        }
    }

    @Override
    public float getKcalConsumedForItemAndQuantity(UUID itemId, float quantity, String userid, UUID pantryId) throws ItemNotFoundException {
        Optional<Pantry> optionalPantry = pantryService.getPantryById(userid, pantryId);
        Pantry pantry = optionalPantry.orElseThrow(() -> new PantryNotFoundException("Pantry not found"));
        Item item = itemRepository.findItemByIdAndPantry(itemId, pantry).orElseThrow(() -> new ItemNotFoundException("The item was not found"));

        return foodDetailsIntegratorService.getKcalConsumed(item.getBarcode(), quantity);
    }

    @Override
    public ItemStatisticWrapperDto getItemsStatistic(String userid, UUID pantryId) {
        Optional<Pantry> optionalPantry = pantryService.getPantryById(userid, pantryId);
        Pantry pantry = optionalPantry.orElseThrow(() -> new PantryNotFoundException("Pantry not found"));
        ItemStatisticWrapperDto itemStatisticWrapperDto = new ItemStatisticWrapperDto();
        LocalDate nowPlusOneWeek = LocalDate.now().plusWeeks(1);
        List<ItemDto> itemsInExpiration = itemRepository.findItemsInExpiration(nowPlusOneWeek, pantry).stream().map(itemMapper::entityToDto).toList();
        itemStatisticWrapperDto.setItemsInExpiration(itemsInExpiration);
        List<ItemDto> itemsAlmostFinished = itemRepository.findItemsAlmostFinished(pantry, PageRequest.of(0, 5)).stream().map(itemMapper::entityToDto).toList();
        itemStatisticWrapperDto.setItemsAlmostFinished(itemsAlmostFinished);
        return itemStatisticWrapperDto;
    }

    private void saveFoodDetail(FoodDetailDto foodDetailDto, Item item) {
        boolean isPresent = foodDetailRepository.findFoodDetailByItem(item).isPresent();
        if (!isPresent) {
            FoodDetail foodDetail = foodDetailMapper.dtoToEntity(foodDetailDto, item);
            foodDetail.setItem(item);
            LOGGER.info("Saving food details for item " + item.getBarcode());
            foodDetail = foodDetailRepository.save(foodDetail);
            item.setFoodDetail(foodDetail);
            itemRepository.save(item);
        }
    }

}
