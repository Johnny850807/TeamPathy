package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ActivityScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.UserNotFoundException;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignIn;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.MainPresenter;

import javax.inject.Inject;

@ActivityScope
public class MainPresenterImp implements MainPresenter {
    private MainView mainView;
    private SignIn signIn;

    @Inject
    public MainPresenterImp(SignIn signIn) {
        this.signIn = signIn;
    }

    @Override
    public void signIn(String account, String password) {
        SignIn.Params params = new SignIn.Params(account,password);
        signIn.execute(new DefaultObserver<User>() {
            @Override
            public void onNext(User user) {
                mainView.signInSuccessfully(user);
            }

            @Override
            public void onError(Throwable exception) {
                if (exception instanceof UserNotFoundException)
                    mainView.onUserNotFound();
            }
        }, params);
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onResume() {}

    @Override
    public void onDestroy() {
        signIn.dispose();
    }
}
