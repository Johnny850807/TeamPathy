package com.ood.clean.waterball.teampathy.Domain.UseCase.User;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ActivityScope;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.UserRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObserverFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@ActivityScope
public class SignUp extends UseCase<User, SignUp.Params> {

    private UserRepository userRepository;

    @Inject
    public SignUp(ThreadingObserverFactory threadingObserverFactory, UserRepository userRepository) {
        super(threadingObserverFactory);
        this.userRepository = userRepository;
    }

    @Override
    protected Observable<User> buildUseCaseObservable(final SignUp.Params params){
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<User> e) throws Exception {
                e.onNext(userRepository.signUp(params));
                e.onComplete();
            }
        });
    }

    public static class Params {
        private String account;
        private String password;
        private String name;
        private String imageUrl;
        private String pushNotificationToken;

        public Params(String account, String password, String name, String imageUrl, String pushNotificationToken) {
            this.account = account;
            this.password = password;
            this.name = name;
            this.imageUrl = imageUrl;
            this.pushNotificationToken = pushNotificationToken;
        }

        public String getPushNotificationToken() {
            return pushNotificationToken;
        }

        public void setPushNotificationToken(String pushNotificationToken) {
            this.pushNotificationToken = pushNotificationToken;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getAccount() {
            return account;
        }

        public String getPassword() {
            return password;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


}
