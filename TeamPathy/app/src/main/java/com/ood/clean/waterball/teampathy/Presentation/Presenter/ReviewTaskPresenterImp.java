package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.ReviewTaskCard;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs.GetReviewTaskCards;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.ReviewTaskPresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;


public class ReviewTaskPresenterImp implements ReviewTaskPresenter {
    @Inject GetReviewTaskCards getReviewTaskCards;
    @Inject Project project;
    ReviewTaskView reviewTaskView;

    @Inject
    public ReviewTaskPresenterImp(){}

    @Override
    public void loadReviewTaskCard() {
        getReviewTaskCards.execute(new DefaultObserver<ReviewTaskCard>() {
            @Override
            public void onNext(@NonNull ReviewTaskCard card) {
                reviewTaskView.onLoadReviewTaskCard(card);
            }

            @Override
            public void onComplete() {
                reviewTaskView.onReviewTasksLoadComplete();
            }

            @Override
            public void onError(Throwable exception) {
                reviewTaskView.onOperationError(exception);
            }
        }, project);
    }

    public void setReviewTaskView(ReviewTaskView reviewTaskView) {
        this.reviewTaskView = reviewTaskView;
    }
}
