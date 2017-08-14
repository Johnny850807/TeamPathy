package com.ood.clean.waterball.teampathy.Domain.DI.Component;

import com.ood.clean.waterball.teampathy.Domain.DI.Module.ActivityModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Module.ApplicationModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit.RetrofitApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = { ApplicationModule.class,
                RetrofitApplicationModule.class }
)
public interface ApplicationComponent {
    ActivityComponent plus(ActivityModule activityModule);
}
