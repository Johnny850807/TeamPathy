package com.ood.clean.waterball.teampathy.Domain.UseCase.User;


import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.UserRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObserverFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/** 要做的事情
* 1. 宣告 使用案例繼承 UseCase<回傳,參數>，並複寫 alt + enter
 * 2. 宣告參數 Params 靜態類別 並填入適當參數 跟 getter setter constructor
 * 3. 針對這個使用案例，思考是否要開新的Repository。 (SignIn 需要 UserRepository 對 User 做 登入的資料庫動作)
 * 4. 若要開→ 開新的類別，考慮是否繼承Repository(函數是否適當或者開一個就好)
 * 5. 回到使用案例，宣告該Repository (注意是針對抽象的做宣告)
 * 6. 在 buildUseCaseObservable 中 做完使用案例並回傳Repository給的值
 */

public class SignIn extends UseCase<User,SignIn.Params> {
    private UserRepository userRepository;

    @Inject
    public SignIn(ThreadingObserverFactory threadingObserverFactory,
                  UserRepository userRepository) {
        super(threadingObserverFactory);
        this.userRepository = userRepository;
    }

    @Override
    protected Observable<User> buildUseCaseObservable(final Params params) {
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<User> e) throws Exception {
                e.onNext(userRepository.signIn(params));
                e.onComplete();
            }
        });
    }

    public static class Params{
        private String account;
        private String password;
        private String pushNotificationToken;

        public Params(String account, String password, String pushNotificationToken) {
            this.account = account;
            this.password = password;
            this.pushNotificationToken = pushNotificationToken;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPushNotificationToken() {
            return pushNotificationToken;
        }

        public void setPushNotificationToken(String pushNotificationToken) {
            this.pushNotificationToken = pushNotificationToken;
        }

        @Override
        public boolean equals(Object obj) {
            SignIn.Params params = (Params) obj;
            return account.equals(params.account) && password.equals(params.password);
        }

        @Override
        public int hashCode() {
            return (account+password).hashCode();
        }
    }

}
