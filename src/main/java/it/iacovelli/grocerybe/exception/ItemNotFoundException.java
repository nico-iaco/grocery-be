package it.iacovelli.grocerybe.exception;

public class ItemNotFoundException extends RuntimeException{

    private String msg;

    public ItemNotFoundException(String msg) {
        super(msg);
    }

}
