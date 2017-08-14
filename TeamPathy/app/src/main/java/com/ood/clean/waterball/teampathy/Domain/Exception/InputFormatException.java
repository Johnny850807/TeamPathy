package com.ood.clean.waterball.teampathy.Domain.Exception;

/**
 * This exception will be throwed when the input format from the user is incorrect,
 * such like a sign-up action with the account and password input which might be in the wrong length.
 */

public class InputFormatException extends RuntimeException{

    public InputFormatException(String message) {
        super(message);
    }

}
