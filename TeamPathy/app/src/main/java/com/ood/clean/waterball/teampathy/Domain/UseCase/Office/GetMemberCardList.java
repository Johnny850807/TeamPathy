package com.ood.clean.waterball.teampathy.Domain.UseCase.Office;


import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Repository.OfficeRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@ProjectScope
public class GetMemberCardList extends UseCase<MemberIdCard, Project> {

    private OfficeRepository officeRepository;

    @Inject
    public GetMemberCardList(ThreadingObservableFactory threadingObservableFactory, OfficeRepository officeRepository) {
        super(threadingObservableFactory);
        this.officeRepository = officeRepository;
    }

    @Override
    protected Observable<MemberIdCard> buildUseCaseObservable(final Project project) {
        return Observable.create(new ObservableOnSubscribe<MemberIdCard>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<MemberIdCard> e) throws Exception {
                List<MemberIdCard> memberIdCardList = officeRepository.getMemberIdCardList();
                for ( MemberIdCard card : memberIdCardList )
                    e.onNext(card);
                e.onComplete();
            }
        });
    }
}
