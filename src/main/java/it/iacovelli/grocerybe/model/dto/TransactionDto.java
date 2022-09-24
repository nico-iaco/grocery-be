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

    private String vendor;

    private double quantity;

    private double availableQuantity;

    private String unit;

    private double price;

    private LocalDate expirationDate;

}
