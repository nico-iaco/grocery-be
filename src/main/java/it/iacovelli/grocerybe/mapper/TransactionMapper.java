package it.iacovelli.grocerybe.mapper;

import it.iacovelli.grocerybe.model.Item;
import it.iacovelli.grocerybe.model.Transaction;
import it.iacovelli.grocerybe.model.dto.TransactionDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", source = "transactionDto.id")
    Transaction dtoToEntity(TransactionDto transactionDto, Item item);

    TransactionDto entityToDto(Transaction transaction);

    @Mapping(target = "item", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTransaction(TransactionDto transactionDto, @MappingTarget Transaction transaction);

}
