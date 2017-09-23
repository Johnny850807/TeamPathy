package com.ood.clean.waterball.teampathy.Presentation.Interfaces;


import com.ood.clean.waterball.teampathy.Domain.Model.ReviewTaskCard;

public interface ReviewTaskPresenter {
    void loadReviewTaskCard();

    public interface ReviewTaskView{
        void onLoadReviewTaskCard(ReviewTaskCard card);
        void onReviewTasksLoadComplete();
        void onOperationError(Throwable err);
    }
}
