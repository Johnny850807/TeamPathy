package com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ood.clean.waterball.teampathy.Domain.Exception.validator.ExceptionValidator;
import com.ood.clean.waterball.teampathy.Domain.ServerConstant;
import com.ood.clean.waterball.teampathy.Domain.Repository.ImageUploadRepository;
import com.ood.clean.waterball.teampathy.Domain.Repository.UserRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.ImgurRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.UserRetrofitRespository;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
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
    @Named("DateFormatRetrofit")
    public Retrofit provideRetrofit(Retrofit.Builder builder){
        Gson gson = new GsonBuilder()  //to correctly parse the date from json
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        return builder.addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    @Named("WbsRetrofit")
    public Retrofit provideWbsRetrofit(Retrofit.Builder builder){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy dd MMM.")
                .create();

        return builder.addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    public Retrofit.Builder provideRetrofitBuilder(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(ServerConstant.BASE_SERVER_API_URL);
    }

    @Provides
    @Singleton
    public UserRepository provideUserRepository(@Named("DateFormatRetrofit") Retrofit retrofit,
                                                ExceptionValidator exceptionValidator){
        return new UserRetrofitRespository(retrofit, exceptionValidator);
    }

    @Provides @Singleton
    public ImageUploadRepository provideImageUploadRepository(){
        return new ImgurRepository(new Retrofit.Builder()
                .baseUrl(ImgurRepository.ImgurAPI.SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build());
    }

}
