package it.iacovelli.grocerybe.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class TransactionDto implements Serializable {

    private final UUID id;

    private final String vendor;

    private final double quantity;

    private final String unit;

    private final double price;

    private final LocalDate expirationDate;

}
