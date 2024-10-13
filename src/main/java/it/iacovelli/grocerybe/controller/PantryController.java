package it.iacovelli.grocerybe.controller;

import it.iacovelli.grocerybe.model.dto.PantryDto;
import it.iacovelli.grocerybe.model.response.BaseResponse;
import it.iacovelli.grocerybe.service.PantryService;
import it.iacovelli.grocerybe.utils.FirebaseUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pantry")
@RequiredArgsConstructor
public class PantryController extends BaseController {

    private final PantryService pantryService;

    private final FirebaseUtils firebaseUtils;

    @GetMapping("/")
    public BaseResponse<List<PantryDto>> getUserPantries(@RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        return new BaseResponse<>(pantryService.getUserPantries(userId), null);
    }

    @PostMapping("/")
    public BaseResponse<PantryDto> addPantry(@RequestBody PantryDto pantryDto, @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        return new BaseResponse<>(pantryService.addPantry(pantryDto, userId), null);
    }

    @PostMapping("/{pantryId}/share")
    public BaseResponse<PantryDto> sharePantry(@PathVariable UUID pantryId, @RequestHeader("Authorization") String token) {
        String userId = firebaseUtils.verifyTokenAndGetUserid(token);
        return new BaseResponse<>(pantryService.sharePantry(pantryId, userId), null);
    }

}
