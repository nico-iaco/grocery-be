package it.iacovelli.grocerybe.mapper;

import it.iacovelli.grocerybe.model.FoodDetail;
import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.dto.FoodDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class FoodDetailMapper {

    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "nutriments", ignore = true)
    public abstract FoodDetailDto entityToDto(FoodDetail foodDetail);

    @Mapping(target = "id", ignore = true)
    public abstract FoodDetail dtoToEntity(FoodDetailDto foodDetailDto, Item item);

}
