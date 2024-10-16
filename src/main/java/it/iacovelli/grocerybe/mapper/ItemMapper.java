package it.iacovelli.grocerybe.mapper;

import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {

    @Autowired
    private TransactionRepository transactionRepository;

    @Mapping(target = "nextExpirationDate", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "pantryId", source = "pantry.id")
    @Mapping(target = "availableQuantity", ignore = true)
    public abstract ItemDto entityToDto(Item item);

    @Mapping(target = "foodDetail", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pantry", ignore = true)
    @Mapping(target = "transactionList", ignore = true)
    public abstract Item dtoToEntity(ItemDto itemDto);

    @Mapping(target = "foodDetail", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pantry", ignore = true)
    @Mapping(target = "transactionList", ignore = true)
    public abstract void updateItem(ItemDto itemDto, @MappingTarget Item item);

    @BeforeMapping
    @Transactional
    public void enrichItemDto(Item item, @MappingTarget ItemDto itemDto) {
        double quantity = transactionRepository.sumItemQuantityByItem(item).orElse(0.0);
        double availableQuantity = transactionRepository.sumItemAvailableQuantityByItem(item).orElse(0.0);
        String unit = transactionRepository.getUnitOfTransaction(item).stream().findFirst().orElse("");
        LocalDate nextExpirationDate = transactionRepository.findNextExpirationDateOfItem(item).stream().findFirst().orElse(null);
        itemDto.setQuantity(quantity);
        itemDto.setAvailableQuantity(availableQuantity);
        itemDto.setUnit(unit);
        itemDto.setNextExpirationDate(nextExpirationDate);
    }

}
