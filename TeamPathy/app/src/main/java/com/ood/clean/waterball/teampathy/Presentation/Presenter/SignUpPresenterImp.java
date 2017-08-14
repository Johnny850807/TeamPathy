package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ActivityScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.AccountDuplicatedException;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignUp;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.SignUpPresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

@ActivityScope
public class SignUpPresenterImp implements SignUpPresenter{
    private SignUpView signUpView;
    private SignUp signUp;

    @Inject
    public SignUpPresenterImp(SignUp signUp) {
        this.signUp = signUp;
    }

    public void setSignUpView(SignUpView signUpView) {
        this.signUpView = signUpView;
    }

    @Override
    public void signUp(SignUp.Params params) {

        signUp.execute(new DefaultObserver<User>() {
            @Override
            public void onNext(@NonNull User user) {
                signUpView.onSignUpSuccessfully(user);
            }

            @Override
            public void onError(Throwable exception) {
                if (exception instanceof AccountDuplicatedException)
                    signUpView.onAccountDuplicated();
            }
        }, params);
    }


}
