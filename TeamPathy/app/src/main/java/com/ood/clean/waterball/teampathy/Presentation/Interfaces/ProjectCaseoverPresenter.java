package com.ood.clean.waterball.teampathy.Presentation.Interfaces;


import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.ProjectProgressInfo;

public interface ProjectCaseoverPresenter extends LifetimePresenter{
    void loadProjectProgressInfo(Project project);
    void doTheProjectCaseover(Project project);

    public interface ProjectCaseoverView
    {
        void onProjectProgressInfoLoaded(ProjectProgressInfo projectProgressInfo);
        void onCaseoverDone();
        void onError(Throwable err);
    }
}
