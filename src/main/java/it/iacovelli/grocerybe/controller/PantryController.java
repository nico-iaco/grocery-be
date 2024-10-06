package it.iacovelli.grocerybe.controller;

import it.iacovelli.grocerybe.model.dto.PantryDto;
import it.iacovelli.grocerybe.service.PantryService;
import it.iacovelli.grocerybe.utils.FirebaseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pantry")
@RequiredArgsConstructor
public class PantryController extends BaseController {

    private final PantryService pantryService;

    private final FirebaseUtils firebaseUtils;

    @GetMapping("/")
    public ResponseEntity<List<PantryDto>> getUserPantries(@RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        return ResponseEntity.ok(pantryService.getUserPantries(userId));
    }

    @PostMapping("/")
    public ResponseEntity<PantryDto> addPantry(@RequestBody PantryDto pantryDto, @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        return ResponseEntity.ok(pantryService.addPantry(pantryDto, userId));
    }

}
