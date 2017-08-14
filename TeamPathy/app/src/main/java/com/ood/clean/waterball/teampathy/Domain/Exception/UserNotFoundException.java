package com.ood.clean.waterball.teampathy.Domain.Exception;


public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super("Cannot find the user, please check if your account or password is correct.");
    }
}
