package com.ood.clean.waterball.teampathy.Domain.DI.Module;

import android.app.Activity;
import android.content.Context;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private Activity activity;
    public ActivityModule(Activity activity){
        this.activity = activity;
    }

    @Provides @ActivityScope
    public Activity provideActivity(){
        return activity;
    }

    @Provides @ActivityScope
    public Context provideContext(){
        return activity.getApplicationContext();
    }

}
