package com.ood.clean.waterball.teampathy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ood.clean.waterball.teampathy.Domain.ServerConstant;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class TestRetrofit {
    private Retrofit retrofit;

    @Before
    public void init(){
        retrofit = provideRetrofit();
    }

    @Test
    public void test() throws Exception{
        String pushNotificationToken = "dc9sJun6TAg:APA91bGZIaYswIt9jYkL_lOFgWSC8QkNiBwBbjSNqwUbX5vCQ6aOglUGmzOpFARQqMVJtiNY5RZb5HKKLPnwc7Cf0iMIVcCFsT03Isiage-nmf6ArHsCyUgQ2w_ioa2N0x_mJ_ZVkt7b";

    }

    private String randomString(){
        StringBuilder strb = new StringBuilder();
        Random random = new Random();
        strb.append("Test");
        for ( int i = 0 ; i < 5 ; i ++ )
            strb.append((char)(97 + random.nextInt(26)));
        return strb.toString();
    }

    private Retrofit provideRetrofit(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();

        return new Retrofit.Builder()
                .baseUrl(ServerConstant.BASE_SERVER_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
