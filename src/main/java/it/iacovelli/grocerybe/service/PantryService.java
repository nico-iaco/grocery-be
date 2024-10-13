package it.iacovelli.grocerybe.service;

import it.iacovelli.grocerybe.model.Pantry;
import it.iacovelli.grocerybe.model.dto.PantryDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PantryService {

    List<PantryDto> getUserPantries(String userId);

    Optional<Pantry> getPantryById(String userId, UUID pantryId);

    Optional<Pantry> getPantryById(UUID pantryId);

    PantryDto addPantry(PantryDto pantryDto, String userId);

    PantryDto updatePantry(UUID pantryId, PantryDto pantryDto, String userId);

    void deletePantry(UUID pantryId, String userId);

    PantryDto sharePantry(UUID pantryId, String userId);

}
