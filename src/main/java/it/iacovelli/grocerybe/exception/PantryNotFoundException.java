package it.iacovelli.grocerybe.exception;

public class PantryNotFoundException extends RuntimeException {
    public PantryNotFoundException(String message) {
        super(message);
    }
}
