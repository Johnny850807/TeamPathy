package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.ProjectsPresenterImp;
import com.ood.clean.waterball.teampathy.R;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class ProjectCaseoverDialogFragment extends DialogFragment {
    @Inject ProjectsPresenterImp presenterImp;
    @Inject Member member;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return TeamPathyDialogFactory.templateBuilder(getActivity())
                .setTitle(R.string.project_caseover)
                .setView(createView())
                .create();
    }

    private View createView(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.project_caseover_dialog, null);
        bindAndInject(view);
        return view;
    }

    private void bindAndInject(View view) {
        MyApp.getProjectComponent(getActivity()).inject(this);
        ButterKnife.bind(this, view);
    }
}
