package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.WbsScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.ProjectProgressInfo;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.DoProjectCaseOver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs.GetWbs;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.ProjectCaseoverPresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;


@WbsScope
public class ProjectCaseoverPresenterImp implements ProjectCaseoverPresenter{
    private ProjectCaseoverView caseoverView;
    @Inject GetWbs getWbs;
    @Inject DoProjectCaseOver doProjectCaseOver;

    @Inject
    public ProjectCaseoverPresenterImp(){}

    public void setCaseoverView(ProjectCaseoverView caseoverView) {
        this.caseoverView = caseoverView;
    }

    @Override
    public void loadProjectProgressInfo(Project project) {
        getWbs.execute(new DefaultObserver<TaskItem>() {
            @Override
            public void onNext(@NonNull TaskItem taskItem) {
                caseoverView.onProjectProgressInfoLoaded(new ProjectProgressInfo(taskItem.toList()));
            }
        }, project);
    }


    @Override
    public void doTheProjectCaseover(Project project) {
        doProjectCaseOver.execute(new DefaultObserver<Void>() {
            @Override
            public void onNext(@NonNull Void aVoid) {
                caseoverView.onCaseoverDone();
            }
        }, project);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        getWbs.dispose();
        doProjectCaseOver.dispose();
    }
}
