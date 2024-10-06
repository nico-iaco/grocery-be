package it.iacovelli.grocerybe.service.impl;

import it.iacovelli.grocerybe.mapper.PantryMapper;
import it.iacovelli.grocerybe.model.Pantry;
import it.iacovelli.grocerybe.model.UserPantry;
import it.iacovelli.grocerybe.model.UserPantryId;
import it.iacovelli.grocerybe.model.dto.PantryDto;
import it.iacovelli.grocerybe.repository.PantryRepository;
import it.iacovelli.grocerybe.repository.UserPantryRepository;
import it.iacovelli.grocerybe.service.PantryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PantryServiceImpl implements PantryService {

    private final PantryRepository pantryRepository;

    private final UserPantryRepository userPantryRepository;

    private final PantryMapper pantryMapper;

    @Override
    @Transactional
    public List<PantryDto> getUserPantries(String userId) {
        return userPantryRepository.findAllById_UserId(userId).stream()
                .map(userPantry -> pantryRepository.findById(userPantry.getId().getPantryId()).orElseThrow())
                .map(pantryMapper::entityToDto).toList();
    }

    @Override
    @Transactional
    public Optional<Pantry> getPantryById(String userId, UUID pantryId) {
        return userPantryRepository.findById(new UserPantryId(userId, pantryId))
                .map(userPantry -> pantryRepository.findById(userPantry.getId().getPantryId()).orElseThrow());
    }

    @Override
    @Transactional
    public Optional<Pantry> getPantryById(UUID pantryId) {
        return userPantryRepository.findDistinctById_PantryId(pantryId)
                .map(userPantry -> pantryRepository.findById(userPantry.getId().getPantryId()).orElseThrow());
    }

    @Override
    @Transactional
    public PantryDto addPantry(PantryDto pantryDto, String userId) {
        Pantry pantry = pantryMapper.dtoToEntity(pantryDto);
        pantry = pantryRepository.save(pantry);
        UserPantryId userPantryId = new UserPantryId(userId, pantry.getId());
        UserPantry userPantry = new UserPantry();
        userPantry.setId(userPantryId);
        userPantryRepository.save(userPantry);
        return pantryMapper.entityToDto(pantry);
    }

    @Override
    public PantryDto updatePantry(UUID pantryId, PantryDto pantryDto, String userId) {
        Pantry pantry = pantryRepository.findById(pantryId).orElseThrow(() -> new RuntimeException("Pantry not found"));
        pantryMapper.updatePantry(pantryDto, pantry);
        pantryRepository.save(pantry);
        return pantryMapper.entityToDto(pantry);
    }

    @Override
    public void deletePantry(UUID pantryId, String userId) {
        userPantryRepository.deleteById(new UserPantryId(userId, pantryId));
        //TODO: delete pantry if no user is using it otherwise just remove the user from the pantry
    }

    @Override
    public void sharePantry(UUID pantryId, String userId) {
        Pantry pantry = pantryRepository.findById(pantryId).orElseThrow(() -> new RuntimeException("Pantry not found"));
        UserPantryId userPantryId = new UserPantryId(userId, pantry.getId());
        UserPantry userPantry = new UserPantry();
        userPantry.setId(userPantryId);
        userPantryRepository.save(userPantry);
    }

}
