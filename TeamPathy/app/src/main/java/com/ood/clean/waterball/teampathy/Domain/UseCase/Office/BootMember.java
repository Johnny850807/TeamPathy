package com.ood.clean.waterball.teampathy.Domain.UseCase.Office;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Repository.OfficeRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;



public class BootMember extends UseCase<Void, Member> {

    private OfficeRepository officeRepository;

    @Inject
    public BootMember(ThreadingObservableFactory threadingObservableFactory, OfficeRepository officeRepository) {
        super(threadingObservableFactory);
        this.officeRepository = officeRepository;
    }

    @Override
    protected Observable<Void> buildUseCaseObservable(final Member member) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> e) throws Exception {
                officeRepository.bootMember(member.getUserId());
                e.onComplete();
            }
        });

    }
}
