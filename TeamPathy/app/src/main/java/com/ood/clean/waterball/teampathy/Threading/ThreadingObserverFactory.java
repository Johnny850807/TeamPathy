package com.ood.clean.waterball.teampathy.Threading;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/*
* The static factory which provides a boilerplate observable which knows to run on the
*  different threads.
 */
@Singleton
public class ThreadingObserverFactory {
    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;

    @Inject
    public ThreadingObserverFactory(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    public <T> Observable<T> create(Observable<T> observable){
        return observable
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler());
    }
}
