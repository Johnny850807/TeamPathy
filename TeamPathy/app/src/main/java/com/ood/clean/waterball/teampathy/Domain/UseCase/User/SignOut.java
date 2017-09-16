package com.ood.clean.waterball.teampathy.Domain.UseCase.User;

import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.UserRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;


public class SignOut extends UseCase<Void,User> {
    private UserRepository userRepository;

    @Inject
    public SignOut(ThreadingObservableFactory threadingObservableFactory,
                   UserRepository userRepository) {
        super(threadingObservableFactory);
        this.userRepository = userRepository;
    }

    @Override
    protected Observable<Void> buildUseCaseObservable(final User user) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> e) throws Exception {
                userRepository.signOut(user);
                e.onComplete();
            }
        });
    }


}
