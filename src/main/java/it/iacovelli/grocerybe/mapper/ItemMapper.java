package it.iacovelli.grocerybe.mapper;

import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.repository.TransactionRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {

    @Autowired
    private TransactionRepository transactionRepository;

    public abstract ItemDto entityToDto(Item item);

    @Mapping(target = "id", ignore = true)
    public abstract Item dtoToEntity(ItemDto itemDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
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
