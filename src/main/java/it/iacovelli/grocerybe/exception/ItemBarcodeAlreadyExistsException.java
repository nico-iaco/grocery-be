package it.iacovelli.grocerybe.exception;

public class ItemBarcodeAlreadyExistsException extends RuntimeException{

    public ItemBarcodeAlreadyExistsException(String msg) {
        super(msg);
    }

}
