package it.iacovelli.grocerybe.exception;

public class ItemNotFoundException extends RuntimeException{

    public ItemNotFoundException(String msg) {
        super(msg);
    }

}
