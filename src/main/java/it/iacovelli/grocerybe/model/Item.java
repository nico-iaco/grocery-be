package it.iacovelli.grocerybe.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Item {

    @Id
    @GeneratedValue
    private UUID id;

    private String barcode;

    private String name;

    @OneToMany
    @ToString.Exclude
    private List<Transaction> transactionList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id.equals(item.id) && barcode.equals(item.barcode) && name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, barcode, name, transactionList);
    }
}
