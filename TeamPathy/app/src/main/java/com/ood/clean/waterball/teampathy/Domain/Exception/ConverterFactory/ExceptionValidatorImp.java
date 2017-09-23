package com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory;


import com.ood.clean.waterball.teampathy.Domain.Exception.AccountDuplicatedException;
import com.ood.clean.waterball.teampathy.Domain.Exception.ProjectPasswordInvalidException;
import com.ood.clean.waterball.teampathy.Domain.Exception.ResourceNotFoundException;
import com.ood.clean.waterball.teampathy.Domain.Exception.UserNotFoundException;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.ResponseModel;

import javax.inject.Singleton;
/**
 * The converter which converts the response message from the API to an specific exception,
 * if isSuccessful() return true, then convert() method should not be invoked.
 * **/
@Singleton
public class ExceptionValidatorImp implements ExceptionValidator {

    @Override
    public void validate(ResponseModel responseModel) {
        String message = responseModel.getMessage();
        message = message.toLowerCase();

        if (responseModel.getStatus() == 200)
            return;  //success

        if (message.contains("project"))
        {
            if (message.contains("not valid"))
                throw new ProjectPasswordInvalidException();
        }
        if (message.contains("cannot"))
        {
            if (message.contains("find"))
                throw new ResourceNotFoundException();
        }
        if (message.contains("user"))
        {
            if (message.contains("not found"))
                throw new UserNotFoundException();
        }
        if (message.contains("account is duplicated"))
            throw new AccountDuplicatedException();

        throw new RuntimeException("Converter Exception : message => " + message + ", cannot be detected.");
    }
}
