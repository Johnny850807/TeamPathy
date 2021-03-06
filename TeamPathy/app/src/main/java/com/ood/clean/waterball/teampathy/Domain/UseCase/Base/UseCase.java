package com.ood.clean.waterball.teampathy.Domain.UseCase.Base;

import android.support.annotation.NonNull;

import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/*
* The base use case which provides an observable to run each request asynchronously.
 */
public abstract class UseCase<T, Params> {
    private final ThreadingObservableFactory threadingObservableFactory;
    private final CompositeDisposable disposables;

    public UseCase(ThreadingObservableFactory threadingObservableFactory) {
        this.threadingObservableFactory = threadingObservableFactory;
        this.disposables = new CompositeDisposable();
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    protected abstract Observable<T> buildUseCaseObservable(Params params);

        /**
         * Executes the current use case.
         *
         * @param observer {@link DisposableObserver} which will be listening to the observable build
         * by {@link #buildUseCaseObservable(Params)} ()} method.
         * @param params Parameters (Optional) used to build/execute this use case.
         */
    public void execute(@NonNull DisposableObserver<T> observer, Params params){
        final Observable<T> observable = threadingObservableFactory.create(this.buildUseCaseObservable(params));
        addDisposable(observable.subscribeWith(observer));
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    private void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }
}