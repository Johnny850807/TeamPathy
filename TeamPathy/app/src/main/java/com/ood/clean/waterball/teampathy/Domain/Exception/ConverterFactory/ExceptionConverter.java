package com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory;


import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.ResponseModel;

public interface ExceptionConverter {
    public boolean isSuccessful(ResponseModel responseModel);
    public RuntimeException convert(ResponseModel responseModel);
}
