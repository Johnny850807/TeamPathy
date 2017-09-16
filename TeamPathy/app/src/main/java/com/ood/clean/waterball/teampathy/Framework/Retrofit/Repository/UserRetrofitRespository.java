package com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository;

import com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory.ExceptionConverter;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.UserRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignIn;
import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignUp;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

@Singleton
public class UserRetrofitRespository implements UserRepository{
    private ExceptionConverter exceptionConverter;
    private UserAPI userAPI;

    @Inject
    public UserRetrofitRespository(Retrofit retrofit,
                                   ExceptionConverter exceptionConverter) {
        userAPI = retrofit.create(UserAPI.class);
        this.exceptionConverter = exceptionConverter;
    }

    @Override
    public User signIn(SignIn.Params params) throws Exception {
        ResponseModel<User> response =  userAPI.signIn(params.getAccount(), params.getPassword(), params.getPushNotificationToken())
                .execute().body();

        exceptionConverter.validate(response);
        return response.getData();
    }

    @Override
    public User signUp(SignUp.Params params) throws Exception {
        ResponseModel<User> response =  userAPI.signUp(params.getName(),
                params.getAccount(),
                params.getPassword(),
                params.getImageUrl(),
                params.getPushNotificationToken()).execute().body();

        exceptionConverter.validate(response);
        return response.getData();
    }

    @Override
    public void signOut(User user) throws Exception {
        //todo
    }

    public interface UserAPI{
        String RESOURCE = "users";

        @FormUrlEncoded
        @POST(RESOURCE + "/signup")
        public Call<ResponseModel<User>> signUp(@Field("name") String name,
                                 @Field("account") String account,
                                 @Field("password") String password,
                                 @Field("imageUrl") String imageUrl,
                                 @Field("firebaseToken") String token);

        @FormUrlEncoded
        @POST(RESOURCE + "/signin")
        public Call<ResponseModel<User>> signIn(@Field("account") String account,
                           @Field("password") String password,
                           @Field("firebaseToken") String token);
    }
}
