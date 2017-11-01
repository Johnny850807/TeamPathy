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
                projectCaseoverPresenterImp.doTheProjectCaseover(project);
            }
        };
    }

    @Override
    public void onCaseoverDone() {
        TeamPathyDialogFactory.templateBuilder(getActivity())
                .setTitle("專案已成功結案！")
                .setMessage("所有獎勵值都已結算！可在辦公室的成員卡片上面看見各項獎勵值，恭喜！")
                .setPositiveButton(R.string.confirm, null)
                .show();
    }
}
