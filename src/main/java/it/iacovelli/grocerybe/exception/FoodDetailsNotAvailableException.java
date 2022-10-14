package it.iacovelli.grocerybe.exception;

public class FoodDetailsNotAvailableException extends RuntimeException {

    public FoodDetailsNotAvailableException(String message) {
        super(message);
    }

}
