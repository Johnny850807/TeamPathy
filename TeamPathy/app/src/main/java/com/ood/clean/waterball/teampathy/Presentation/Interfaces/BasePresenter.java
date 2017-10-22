package com.ood.clean.waterball.teampathy.Presentation.Interfaces;


import com.ood.clean.waterball.teampathy.MyUtils.PageController;

public interface BasePresenter {

    public interface BaseView {
        void setToolbarTitle(String title);

        void showProgressBar();

        void hideProgressBar();

        void showProgressDialog();

        void hideProgressDialog();

        void finish();

        PageController getPageController();

        void createAndShowDialogForOptionsOfTakePicture();
    }
}
