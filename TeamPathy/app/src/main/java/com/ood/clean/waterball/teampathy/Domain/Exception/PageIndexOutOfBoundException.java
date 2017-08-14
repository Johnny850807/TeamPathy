package com.ood.clean.waterball.teampathy.Domain.Exception;



public class PageIndexOutOfBoundException extends RuntimeException {

    public PageIndexOutOfBoundException() {
        super("Page index out of bound.");
    }

    public PageIndexOutOfBoundException(String message) {
        super(message);
    }
}
