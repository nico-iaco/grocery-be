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
public class TransactionDto implements Serializable {

    private UUID id;

    private String seller;

    private double quantity;

    private double quantityStd;

    private double availableQuantity;

    private String unit;

    private double price;

    private LocalDate expirationDate;

    private LocalDate purchaseDate;

}
