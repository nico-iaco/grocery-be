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

    @JoinColumn(name = "userId")
    private String userId;

    @JoinColumn(name = "pantryId")
    private UUID pantryId;
}
