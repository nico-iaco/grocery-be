package it.iacovelli.grocerybe.mapper;

import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.dto.ItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto entityToDto(Item item);

    @Mapping(target = "id", ignore = true)
    Item dtoToEntity(ItemDto itemDto);

    @Mapping(target = "id", ignore = true)
    void updateItem(ItemDto itemDto, @MappingTarget Item item);

}
