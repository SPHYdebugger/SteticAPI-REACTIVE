package com.home.SteticApi.exception.ClientException;


public class ClientNotFoundException extends Exception {

    public ClientNotFoundException() {
        super();
    }

    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(long id) {
        super("The client " + id + " doesn't exist");
    }

}
