package com.sphy.SteticApi.Reactive.exception.ProductException;


public class ProductNotFoundException extends Exception {

    public ProductNotFoundException() {
        super();
    }

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(long id) {
        super("The product " + id + " doesn't exist");
    }
}
