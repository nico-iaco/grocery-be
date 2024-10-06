package it.iacovelli.grocerybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class UserPantry {

    @EmbeddedId
    private UserPantryId id;

}
