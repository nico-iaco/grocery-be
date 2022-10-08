package it.iacovelli.grocerybe.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ItemStatisticDto implements Serializable {

    private List<ItemDto> itemsAlmostFinished;

    private List<ItemDto> itemsInExpiration;

}
