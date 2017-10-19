package com.ood.clean.waterball.teampathy.Domain.Exception.validator;


import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.ResponseModel;

public interface ExceptionValidator {
    public void validate(ResponseModel responseModel);
}
