package com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory;


import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.ResponseModel;

public interface ExceptionValidator {
    public void validate(ResponseModel responseModel);
}
