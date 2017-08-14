package com.ood.clean.waterball.teampathy.Threading;


import android.support.annotation.NonNull;

import javax.inject.Singleton;

@Singleton
public class JobThread implements ThreadExecutor {

    @Override public void execute(@NonNull Runnable runnable) {
        new Thread(runnable).start();
    }


}