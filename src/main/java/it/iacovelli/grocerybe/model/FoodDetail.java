package it.iacovelli.grocerybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity(name = "food_detail")
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
