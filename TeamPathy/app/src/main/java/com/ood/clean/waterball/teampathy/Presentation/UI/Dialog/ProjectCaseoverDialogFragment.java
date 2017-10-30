package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.BasePresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.ProjectCaseoverPresenterImp;
import com.ood.clean.waterball.teampathy.R;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class ProjectCaseoverDialogFragment extends MakeSureToCancelBaseDialogFragment {
    private BasePresenter.BaseView baseView;
    @Inject ProjectCaseoverPresenterImp projectCaseoverPresenterImp;
    @Inject Member member;

    public void setBaseView(BasePresenter.BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    protected Dialog onCustomDialogSetting(AlertDialog alertDialog) {
        alertDialog.setTitle(R.string.project_caseover);
        return alertDialog;
    }

    @Override
    protected String getPositiveButtonText() {
        return getString(R.string.confirm_to_close_case);
    }

    @Override
    protected View onViewCreated() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.project_caseover_dialog, null);
        bindAndInject(view);
        return view;
    }

    private void bindAndInject(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected View.OnClickListener getOnPositiveButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }



}
