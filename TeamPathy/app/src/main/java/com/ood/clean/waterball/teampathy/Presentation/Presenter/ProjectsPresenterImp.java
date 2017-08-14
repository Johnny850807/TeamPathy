package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.UserScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.CreateProject;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.GetMemberInfo;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.GetUserProjectList;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.CrudPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.ProjectsPresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

@UserScope
public class ProjectsPresenterImp implements CrudPresenter<Project> {
    private ProjectsPresenter.ProjectView projectsView;
    private GetUserProjectList getUserProjectList;
    private CreateProject createProject;
    private GetMemberInfo getMemberInfo;
    private User user;

    @Inject
    public ProjectsPresenterImp(GetUserProjectList getUserProjectList,
                                CreateProject createProject,
                                GetMemberInfo getMemberInfo, User user) {
        this.getUserProjectList = getUserProjectList;
        this.createProject = createProject;
        this.getMemberInfo = getMemberInfo;
        this.user = user;
    }

    public void setProjectsView(ProjectsPresenter.ProjectView projectsView) {
        this.projectsView = projectsView;
    }

    public ProjectsPresenter.ProjectView getProjectsView() {
        return projectsView;
    }

    @Override
    public void loadEntities(int page) {
        getUserProjectList.execute(new DefaultObserver<Project>() {
            @Override
            public void onNext(@NonNull Project project) {
                projectsView.loadEntity(project);
            }
            @Override
            public void onComplete() {
                projectsView.onLoadFinishNotify();
            }
        },page);
    }

    @Override
    public void create(Project project) {
        createProject.execute(new DefaultObserver<Project>() {
            @Override
            public void onNext(@NonNull Project project) {
                projectsView.onCreateFinishNotify(project);
            }
        },project);
    }


    // get the member info like : position, contribution which user behaves in the project
    public void loadMemberInfo(final Project project){
        getMemberInfo.execute(new DefaultObserver<Member>() {
            @Override
            public void onNext(@NonNull Member member) {
                projectsView.onMemberInfoLoadFinsih(project,member);
            }
        },new GetMemberInfo.Params(user.getId(), project.getId()));
    }

    @Override
    public void update(Project project) {

    }

    @Override
    public void delete(Project project) {

    }

    @Override
    public void onResume() {
    }


    @Override
    public void onDestroy() {
        createProject.dispose();
        getUserProjectList.dispose();
    }

}
