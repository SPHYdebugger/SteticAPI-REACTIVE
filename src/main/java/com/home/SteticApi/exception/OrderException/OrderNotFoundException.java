package com.home.SteticApi.exception.OrderException;


public class OrderNotFoundException extends Exception {

    public OrderNotFoundException() {
        super();
    }

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(long id) {
        super("The order " + id + " doesn't exist");
    }
}