package it.iacovelli.grocerybe.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
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
