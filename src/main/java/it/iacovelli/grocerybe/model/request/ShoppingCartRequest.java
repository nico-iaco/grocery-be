package it.iacovelli.grocerybe.model.request;

import it.iacovelli.grocerybe.model.dto.ShoppingItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartRequest {

    private List<ShoppingItemDto> shoppingItems;

}
