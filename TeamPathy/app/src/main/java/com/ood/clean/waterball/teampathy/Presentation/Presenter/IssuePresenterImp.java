package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Issue.CreateIssue;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Issue.GetIssueList;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.CrudPresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

@ProjectScope
public class IssuePresenterImp implements CrudPresenter<Issue> {
    private CrudView<Issue> issueView;
    private GetIssueList getIssueList;
    private CreateIssue createIssue;

    @Inject
    public IssuePresenterImp(GetIssueList getIssueList, CreateIssue createIssue) {
        this.getIssueList = getIssueList;
        this.createIssue = createIssue;
    }

    public void setIssueView(CrudView<Issue> issueView) {
        this.issueView = issueView;
    }

    @Override
    public void loadEntities(int page) {
        getIssueList.execute(new DefaultObserver<Issue>() {
            @Override
            public void onNext(@NonNull Issue issue) {
                issueView.loadEntity(issue);
            }
            @Override
            public void onComplete() {
                issueView.onLoadFinishNotify();
            }
        },page);
    }

    @Override
    public void create(Issue issue) {
        createIssue.execute(new DefaultObserver<Issue>() {
            @Override
            public void onNext(@NonNull Issue issue) {
                issueView.loadEntity(issue);
                issueView.onCreateFinishNotify(issue);
            }
        },issue);
    }

    @Override
    public void update(Issue issue) {

    }

    @Override
    public void delete(Issue issue) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        createIssue.dispose();
        getIssueList.dispose();
    }
}
