package it.iacovelli.grocerybe.controller;

import it.iacovelli.grocerybe.exception.FoodDetailsNotAvailableException;
import it.iacovelli.grocerybe.exception.ItemBarcodeAlreadyExistsException;
import it.iacovelli.grocerybe.exception.ItemNotFoundException;
import it.iacovelli.grocerybe.exception.UserNotFoundException;
import it.iacovelli.grocerybe.model.response.BaseResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {


    @ExceptionHandler(ItemNotFoundException.class)
    public BaseResponse<String> itemNotFoundHandler(RuntimeException e) {
        return new BaseResponse<>(null, e.getMessage());
    }

    @ExceptionHandler(ItemBarcodeAlreadyExistsException.class)
    public BaseResponse<String> itemBarcodeAlreadyExistsHandler(RuntimeException e) {
        return new BaseResponse<>(null, e.getMessage());
    }

    @ExceptionHandler(FoodDetailsNotAvailableException.class)
    public BaseResponse<String> foodDetailsNotAvailableHandler(RuntimeException e) {
        return new BaseResponse<>(null, e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public BaseResponse<String> userNotFoundHandler(RuntimeException e) {
        return new BaseResponse<>(null, e.getMessage());
    }

}
