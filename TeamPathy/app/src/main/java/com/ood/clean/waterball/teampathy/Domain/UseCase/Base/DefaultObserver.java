package com.ood.clean.waterball.teampathy.Domain.UseCase.Base;

import io.reactivex.observers.DisposableObserver;

public abstract class DefaultObserver<T> extends DisposableObserver<T> {

    @Override public void onComplete() {
        // no-op by default.
    }

    @Override public void onError(Throwable exception) {
        // no-op by default.
    }

}
