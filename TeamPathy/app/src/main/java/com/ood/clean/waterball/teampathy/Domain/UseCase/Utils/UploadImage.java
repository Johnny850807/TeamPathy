package com.ood.clean.waterball.teampathy.Domain.UseCase.Utils;


import com.ood.clean.waterball.teampathy.Domain.Repository.ImageUploadRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObserverFactory;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

public class UploadImage extends UseCase<String,File> {
    private ImageUploadRepository imageUploadRepository;

    @Inject
    public UploadImage(ThreadingObserverFactory threadingObserverFactory,
                       ImageUploadRepository imageUploadRepository) {
        super(threadingObserverFactory);
        this.imageUploadRepository = imageUploadRepository;
    }

    @Override
    protected Observable<String> buildUseCaseObservable(final File file) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext(imageUploadRepository.uploadImage(file));
                e.onComplete();
            }
        });
    }
}
