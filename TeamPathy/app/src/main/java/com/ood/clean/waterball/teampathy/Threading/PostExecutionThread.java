package com.ood.clean.waterball.teampathy.Threading;

import io.reactivex.Scheduler;


public interface PostExecutionThread {
    Scheduler getScheduler();
}
