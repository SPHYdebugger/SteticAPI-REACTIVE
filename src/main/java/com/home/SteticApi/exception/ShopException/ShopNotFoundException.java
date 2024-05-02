package com.home.SteticApi.exception.ShopException;


public class ShopNotFoundException extends Exception {

    public ShopNotFoundException(int shopId) {
        super();
    }

    public ShopNotFoundException(String message) {
        super(message);
    }

    public ShopNotFoundException(long id) {
        super("The shop " + id + " doesn't exist");
    }
}
