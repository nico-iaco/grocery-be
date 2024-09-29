package it.iacovelli.grocerybe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto implements Serializable {

    private UUID id;

    private String userId;

    private String vendor;

    private String barcode;

    private String name;

    private double quantity;

    private double availableQuantity;

    private String unit;

    private LocalDate nextExpirationDate;

}
