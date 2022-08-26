package it.iacovelli.grocerybe.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;


@Data
public class ItemDto implements Serializable {

    private final UUID id;

    private final String barcode;

    private final String name;

}
