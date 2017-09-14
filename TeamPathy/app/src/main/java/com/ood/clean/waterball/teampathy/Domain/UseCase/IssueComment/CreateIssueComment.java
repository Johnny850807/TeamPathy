package com.ood.clean.waterball.teampathy.Domain.UseCase.IssueComment;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.IssueScope;
import com.ood.clean.waterball.teampathy.Domain.Model.IssueComment;
import com.ood.clean.waterball.teampathy.Domain.Repository.IssueCommentRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by User on 2017/8/1.
 */
@IssueScope
public class CreateIssueComment extends UseCase<IssueComment, IssueComment> {
    private IssueCommentRepository issueCommentRepository;

    @Inject
    public CreateIssueComment(ThreadingObservableFactory threadingObservableFactory,
                              IssueCommentRepository issueCommentRepository) {
        super(threadingObservableFactory);
        this.issueCommentRepository = issueCommentRepository;
    }

    @Override
    protected Observable<IssueComment> buildUseCaseObservable(final IssueComment issueComment) {
        return Observable.create(new ObservableOnSubscribe<IssueComment>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<IssueComment> e) throws Exception {
                e.onNext(issueCommentRepository.create(issueComment));
                e.onComplete();
            }
        });
    }
}
