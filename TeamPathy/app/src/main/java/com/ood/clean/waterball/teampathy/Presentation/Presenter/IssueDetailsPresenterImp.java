package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.IssueScope;
import com.ood.clean.waterball.teampathy.Domain.Model.IssueComment;
import com.ood.clean.waterball.teampathy.Domain.UseCase.IssueComment.CreateIssueComment;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.IssueComment.GetIssueCommentList;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.CrudPresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

@IssueScope
public class IssueDetailsPresenterImp implements CrudPresenter<IssueComment> {
    private CrudView<IssueComment> issueDetailsView;
    private GetIssueCommentList getIssueCommentList;
    private CreateIssueComment createIssueComment;

    @Inject
    public IssueDetailsPresenterImp(GetIssueCommentList getIssueCommentList,
                                    CreateIssueComment createIssueComment) {
        this.getIssueCommentList = getIssueCommentList;
        this.createIssueComment = createIssueComment;
    }

    public void setIssueDetailsView(CrudView<IssueComment> issueDetailsView) {
        this.issueDetailsView = issueDetailsView;
    }

    @Override
    public void loadEntities(int page) {
        getIssueCommentList.execute(new DefaultObserver<IssueComment>() {
            @Override
            public void onNext(@NonNull IssueComment issueComment) {
                issueDetailsView.loadEntity(issueComment);
            }
            @Override
            public void onComplete() {
                issueDetailsView.onLoadFinishNotify();
            }
        },page);
    }

    @Override
    public void create(final IssueComment issueComment) {
        createIssueComment.execute(new DefaultObserver<IssueComment>() {
            @Override
            public void onNext(@NonNull IssueComment issueComment) {
                issueDetailsView.loadEntity(issueComment);
                issueDetailsView.onCreateFinishNotify(issueComment);
            }
        }, issueComment);
    }

    @Override
    public void update(IssueComment issueComment) {

    }

    @Override
    public void delete(IssueComment issueComment) {

    }

    @Override
    public void onResume() {}

    @Override
    public void onDestroy() {
        getIssueCommentList.dispose();
        createIssueComment.dispose();
    }


}
