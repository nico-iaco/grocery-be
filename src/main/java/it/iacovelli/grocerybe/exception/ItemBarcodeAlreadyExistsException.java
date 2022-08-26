package it.iacovelli.grocerybe.exception;

public class ItemBarcodeAlreadyExistsException extends RuntimeException{

    private String msg;

    public ItemBarcodeAlreadyExistsException(String msg) {
        super(msg);
    }

}
