package it.iacovelli.grocerybe.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserPantryId implements Serializable {

    @JoinColumn(name = "user_id")
    private String userId;

    @JoinColumn(name = "pantry_id")
    private UUID pantryId;
}
