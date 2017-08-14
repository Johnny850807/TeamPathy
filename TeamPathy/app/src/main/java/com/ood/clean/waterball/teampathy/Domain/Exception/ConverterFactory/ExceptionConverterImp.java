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
public class ExceptionConverterImp implements ExceptionConverter {

    @Override
    public boolean isSuccessful(ResponseModel responseModel) {
        return responseModel.getStatus() == 200;
    }

    @Override
    public RuntimeException convert(ResponseModel responseModel) {
        String message = responseModel.getMessage();
        message = message.toLowerCase();
        if (message.contains("project"))
        {
            if (message.contains("not valid"))
                return new ProjectPasswordInvalidException();
        }
        if (message.contains("cannot"))
        {
            if (message.contains("find"))
                return new ResourceNotFoundException();
        }
        if (message.contains("user"))
        {
            if (message.contains("not found"))
                return new UserNotFoundException();
        }
        if (message.contains("account is duplicated"))
            return new AccountDuplicatedException();

        throw new RuntimeException("Converter Exception : message => " + message + ", cannot be detected.");
    }

}
