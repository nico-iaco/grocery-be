package it.iacovelli.grocerybe.repository;

import it.iacovelli.grocerybe.model.Pantry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PantryRepository extends JpaRepository<Pantry, UUID> {

}
