package com.ood.clean.waterball.teampathy.Presentation.Presenter;


import com.ood.clean.waterball.teampathy.Domain.DI.Scope.UserScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.ProjectPasswordInvalidException;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.JoinProject;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.SearchProjectByName;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.LifetimePresenter;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.ProjectsPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.SearchProjectView;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

@UserScope
public class SearchProjectDialogPresenterImp implements LifetimePresenter{
    private SearchProjectByName searchProjectByName;
    private SearchProjectView searchProjectView;
    private ProjectsPresenter.ProjectView projectView;
    private JoinProject joinProject;

    @Inject
    public SearchProjectDialogPresenterImp(SearchProjectByName searchProjectByName
            , JoinProject joinProject) {
        this.searchProjectByName = searchProjectByName;
        this.joinProject = joinProject;
    }

    public void setSearchProjectView(SearchProjectView searchProjectView) {
        this.searchProjectView = searchProjectView;
    }

    public void setProjectView(ProjectsPresenter.ProjectView projectView) {
        this.projectView = projectView;
    }

    public void searchProjectByName(String name){
        searchProjectByName.execute(new DefaultObserver<Project>() {
            @Override
            public void onNext(@NonNull Project project) {
                searchProjectView.loadEntity(project);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                searchProjectView.onSearchFinish();
            }
        },name);
    }

    public void joinProject(JoinProject.Params params){
        joinProject.execute(new DefaultObserver<Project>() {
            @Override
            public void onNext(@NonNull Project project) {
                projectView.loadEntity(project);
                projectView.onJoinProjectFinish(project);
            }

            @Override
            public void onError(Throwable exception) {
                if (exception instanceof ProjectPasswordInvalidException)
                    searchProjectView.onProjectPasswordInvalid();
            }
        }, params);
    }


    @Override
    public void onResume() {}

    @Override
    public void onDestroy() {
        searchProjectByName.dispose();
    }
}
