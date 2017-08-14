package com.ood.clean.waterball.teampathy.Domain.Exception;


public class AccountDuplicatedException extends RuntimeException{
    public AccountDuplicatedException() {
        super("Account is duplicated, please change a new one.");
    }

    public AccountDuplicatedException(String message) {
        super(message);
    }
}
