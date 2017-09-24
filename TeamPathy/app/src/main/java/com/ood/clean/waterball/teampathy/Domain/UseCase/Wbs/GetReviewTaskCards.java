package com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.WbsScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.ReviewTaskCard;
import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@WbsScope
public class GetReviewTaskCards extends UseCase<ReviewTaskCard,Project> {
    private WbsRepository wbsRepository;

    @Inject
    public GetReviewTaskCards(ThreadingObservableFactory threadingObservableFactory, WbsRepository wbsRepository) {
        super(threadingObservableFactory);
        this.wbsRepository = wbsRepository;
    }

    @Override
    protected Observable<ReviewTaskCard> buildUseCaseObservable(Project project) {
        return Observable.create(new ObservableOnSubscribe<ReviewTaskCard>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ReviewTaskCard> e) throws Exception {
                try{
                    List<ReviewTaskCard> cardList = wbsRepository.getReviewTaskCards();
                    for (ReviewTaskCard card : cardList)
                        e.onNext(card);
                    e.onComplete();
                }catch (Exception err){
                    e.onError(err);
                }
            }
        });
    }
}
