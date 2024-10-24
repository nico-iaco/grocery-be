package it.iacovelli.grocerybe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RegisterReflectionForBinding({UUID[].class})
public class Pantry {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID")
    private UUID id;

    private String name;

    private String description;

}
