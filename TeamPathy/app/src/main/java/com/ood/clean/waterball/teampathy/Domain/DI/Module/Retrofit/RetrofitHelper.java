package com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ood.clean.waterball.teampathy.Domain.ServerConstant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitHelper {
    public static Retrofit provideWbsRetrofit(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy dd MMM.")
                .create();

        return new Retrofit.Builder()
                .baseUrl(ServerConstant.BASE_SERVER_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
