package com.ood.clean.waterball.teampathy.Domain.DI.Component;

import com.ood.clean.waterball.teampathy.Domain.DI.Module.ActivityModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit.RetrofitUserModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ActivityScope;
import com.ood.clean.waterball.teampathy.Presentation.UI.Activity.MainActivity;
import com.ood.clean.waterball.teampathy.Presentation.UI.Activity.SignUpActivity;

import dagger.Subcomponent;


@ActivityScope
@Subcomponent(
        modules = ActivityModule.class
)
public interface ActivityComponent {
    UserComponent plus(RetrofitUserModule retrofitUserModule);

    public void inject(MainActivity mainActivity);
    public void inject(SignUpActivity signUpActivity);
}
