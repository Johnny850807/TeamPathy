package com.ood.clean.waterball.teampathy.Presentation.Interfaces;


import com.ood.clean.waterball.teampathy.Domain.Model.User;

public interface MainPresenter extends LifetimePresenter{

    public interface MainView{
        void signInSuccessfully(User user);
        void onUserNotFound();
        void onPushNotificationNotPrepared();
    }

    void signIn(String account, String password);

}
