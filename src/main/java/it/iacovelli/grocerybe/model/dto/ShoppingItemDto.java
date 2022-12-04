package it.iacovelli.grocerybe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingItemDto {

    private ItemDto item;

    private TransactionDto transaction;

}
