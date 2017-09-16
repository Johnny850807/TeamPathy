package com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory;


import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.ResponseModel;

public interface ExceptionConverter {
    public void validate(ResponseModel responseModel);
}
