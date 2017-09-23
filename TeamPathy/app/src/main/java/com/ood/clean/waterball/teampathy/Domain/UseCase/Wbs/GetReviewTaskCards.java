package com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs;

import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.ReviewTaskCard;
import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import io.reactivex.Observable;



public class GetReviewTaskCards extends UseCase<Project,ReviewTaskCard> {
    private WbsRepository wbsRepository;

    public GetReviewTaskCards(ThreadingObservableFactory threadingObservableFactory, WbsRepository wbsRepository) {
        super(threadingObservableFactory);
        this.wbsRepository = wbsRepository;
    }

    @Override
    protected Observable<Project> buildUseCaseObservable(ReviewTaskCard card) {
        return null;
    }
}
