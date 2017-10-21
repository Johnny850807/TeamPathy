package com.ood.clean.waterball.teampathy.Domain.UseCase.Office;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Position;
import com.ood.clean.waterball.teampathy.Domain.Repository.OfficeRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;



public class ChangeMemberPosition extends UseCase<Member, ChangeMemberPosition.Params> {

    private OfficeRepository officeRepository;

    @Inject
    public ChangeMemberPosition(ThreadingObservableFactory threadingObservableFactory, OfficeRepository officeRepository) {
        super(threadingObservableFactory);
        this.officeRepository = officeRepository;
    }

    @Override
    protected Observable<Member> buildUseCaseObservable(final ChangeMemberPosition.Params params) {
        return Observable.create(new ObservableOnSubscribe<Member>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Member> e) throws Exception {
                Member member = officeRepository.changeMemberPosition(params.getUserId(), params.getPosition());
                e.onNext(member);
                e.onComplete();
            }
        });
    }

    public static class Params{
        private Member member;
        private Position position;

        public Params(Member member, Position position) {
            this.member = member;
            this.position = position;
        }

        public Member getMember() {
            return member;
        }


        public Position getPosition() {
            return position;
        }

        public int getUserId(){
            return member.getUserId();
        }
    }
}
