package it.iacovelli.grocerybe.repository;

import it.iacovelli.grocerybe.model.UserPantry;
import it.iacovelli.grocerybe.model.UserPantryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPantryRepository extends JpaRepository<UserPantry, UserPantryId> {

    List<UserPantry> findAllById_UserId(String userId);

    Optional<UserPantry> findDistinctById_PantryId(UUID pantryId);

}
