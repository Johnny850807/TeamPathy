package com.ood.clean.waterball.teampathy.Domain.UseCase.Office;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Position;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.OfficeRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import java.lang.reflect.Member;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by Lin on 2017/7/16.
 */

public class ChangeMemberPosition extends UseCase<Void, ChangeMemberPosition.Params> {

    private OfficeRepository officeRepository;
    private Member member;

    @Inject
    public ChangeMemberPosition(ThreadingObservableFactory threadingObservableFactory, OfficeRepository officeRepository) {
        super(threadingObservableFactory);
        this.officeRepository = officeRepository;
    }

    @Override
    protected Observable<Void> buildUseCaseObservable(final ChangeMemberPosition.Params params) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> e) throws Exception {
                officeRepository.changeMemberPosition(params.getUser().getId(), params.getPosition());
            }
        }); //todo
    }

    public static class Params{
        private User user;
        private Position position;

        public Params(User user, Position position) {
            this.user = user;
            this.position = position;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }
    }
}
