package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.ProjectProgressInfo;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.BasePresenter;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.ProjectCaseoverPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.ProjectCaseoverPresenterImp;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.ProjectCaseoverDialogBinding;

import javax.inject.Inject;


public class ProjectCaseoverDialogFragment extends MakeSureToCancelBaseDialogFragment implements ProjectCaseoverPresenter.ProjectCaseoverView{
    private BasePresenter.BaseView baseView;
    private ProjectCaseoverDialogBinding binding;
    @Inject Project project;
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
        View view = bindAndInject();
        projectCaseoverPresenterImp.loadProjectProgressInfo(project);
        return view;
    }

    private View bindAndInject() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                R.layout.project_caseover_dialog, null, false);
        View view = binding.getRoot();
        MyApp.getWbsComponent(getActivity()).inject(this);
        projectCaseoverPresenterImp.setCaseoverView(this);
        return view;
    }


    @Override
    public void onProjectProgressInfoLoaded(ProjectProgressInfo projectProgressInfo) {
        binding.setObj(projectProgressInfo);
    }


    @Override
    protected View.OnClickListener getOnPositiveButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.getObj().getDonePercentage() == 100)
                {
                    projectCaseoverPresenterImp.doTheProjectCaseover(project);
                    baseView.showProgressDialog();
                }
                else
                    TeamPathyDialogFactory.templateBuilder(getActivity())
                                        .setTitle(R.string.caseover)
                                        .setMessage(R.string.all_task_should_be_done_to_do_caseover)
                                        .show();
            }
        };
    }

    @Override
    public void onCaseoverDone() {
        baseView.hideProgressDialog();
        project.setCaseclosed(true);
        TeamPathyDialogFactory.templateBuilder(getActivity())
                .setTitle(R.string.project_case_closed_successfully)
                .setMessage(R.string.all_exp_calculated_done_congratulation)
                .setPositiveButton(R.string.confirm, null)
                .show();

        getActivity().getSupportFragmentManager().popBackStack();

        dismiss();
    }

    @Override
    public void onError(Throwable err) {
        TeamPathyDialogFactory.templateBuilder(getActivity())
                .setTitle(R.string.error)
                .setMessage(err.getMessage())
                .show();
    }
}
