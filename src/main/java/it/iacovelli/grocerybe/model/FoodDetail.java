package it.iacovelli.grocerybe.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class FoodDetail {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private String imageUrl;

    private String genericName;

    private String imageNutritionUrl;

}
