package it.iacovelli.grocerybe.mapper;

import it.iacovelli.grocerybe.model.Pantry;
import it.iacovelli.grocerybe.model.dto.PantryDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PantryMapper {

    Pantry dtoToEntity(PantryDto pantryDto);

    PantryDto entityToDto(Pantry pantry);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePantry(PantryDto pantryDto, @MappingTarget Pantry pantry);

}
