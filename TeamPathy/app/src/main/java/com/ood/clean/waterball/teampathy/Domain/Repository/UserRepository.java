package com.ood.clean.waterball.teampathy.Domain.Repository;



import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignIn;
import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignUp;
import com.ood.clean.waterball.teampathy.Domain.Model.User;

public interface UserRepository {

    public User signIn(final SignIn.Params params) throws Exception;

    public User signUp(final SignUp.Params params) throws Exception;

    public void signOut(final User user) throws Exception;
}
