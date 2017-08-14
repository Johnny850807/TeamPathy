package com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory.ExceptionConverter;
import com.ood.clean.waterball.teampathy.Domain.ServerConstant;
import com.ood.clean.waterball.teampathy.Domain.Repository.ImageUploadRepository;
import com.ood.clean.waterball.teampathy.Domain.Repository.UserRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.ImgurRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.UserRetrofitRespository;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitApplicationModule {

    @Provides
    @Singleton
    public Retrofit provideRetrofit(){
        Gson gson = new GsonBuilder()  //to correctly parse the date from json
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ServerConstant.BASE_SERVER_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    public UserRepository provideUserRepository(Retrofit retrofit, ExceptionConverter exceptionConverter){
        return new UserRetrofitRespository(retrofit, exceptionConverter);
    }

    @Provides @Singleton
    public ImageUploadRepository provideImageUploadRepository(){
        return new ImgurRepository();
    }

}
