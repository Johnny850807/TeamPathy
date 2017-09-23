package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Presentation.Interfaces.ReviewTaskPresenter;


public class ReviewTaskPresenterImp implements ReviewTaskPresenter {
    private ReviewTaskView reviewTaskView;

    @Override
    public void loadReviewTaskCard() {

    }

    public void setReviewTaskView(ReviewTaskView reviewTaskView) {
        this.reviewTaskView = reviewTaskView;
    }
}
