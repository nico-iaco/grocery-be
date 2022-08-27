package it.iacovelli.grocerybe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto implements Serializable {

    private UUID id;

    private String barcode;

    private String name;

}
