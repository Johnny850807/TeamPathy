package com.ood.clean.waterball.teampathy.Domain.Exception;

/**
 * Created by Lin on 2017/7/16.
 */

public class ProjectPasswordInvalidException extends RuntimeException {

    public ProjectPasswordInvalidException() {
        super ("Project password not conform.");
    }

    public ProjectPasswordInvalidException(String message) {   super(message); }


}
