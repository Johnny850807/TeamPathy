package com.ood.clean.waterball.teampathy.Domain.UseCase.Office;

import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.OfficeRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObserverFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by Lin on 2017/7/16.
 */

public class EvictMember extends UseCase<Void, User> {

    private OfficeRepository officeRepository;

    @Inject
    public EvictMember(ThreadingObserverFactory threadingObserverFactory, OfficeRepository officeRepository) {
        super(threadingObserverFactory);
        this.officeRepository = officeRepository;
    }

    @Override
    protected Observable<Void> buildUseCaseObservable(final User user) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> e) throws Exception {
                officeRepository.evictMember(user.getId());
                e.onComplete();
            }

        });

    }
}
