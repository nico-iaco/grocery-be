package it.iacovelli.grocerybe.mapper;

import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import it.iacovelli.grocerybe.repository.TransactionRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Mapper(componentModel = "spring")
public abstract class ItemMapper {

    @Autowired
    private TransactionRepository transactionRepository;

    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "availableQuantity", ignore = true)
    public abstract ItemDto entityToDto(Item item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionList", ignore = true)
    public abstract Item dtoToEntity(ItemDto itemDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "transactionList", ignore = true)
    public abstract void updateItem(ItemDto itemDto, @MappingTarget Item item);

    @BeforeMapping
    @Transactional
    public void enrichItemDto(Item item, @MappingTarget ItemDto itemDto) {
        double quantity = transactionRepository.sumItemQuantityByItem(item).orElse(0.0);
        double availableQuantity = transactionRepository.sumItemAvailableQuantityByItem(item).orElse(0.0);
        String unit = transactionRepository.getUnitOfTransaction(item).stream().findFirst().orElse("");
        itemDto.setQuantity(quantity);
        itemDto.setAvailableQuantity(availableQuantity);
        itemDto.setUnit(unit);
    }

}
