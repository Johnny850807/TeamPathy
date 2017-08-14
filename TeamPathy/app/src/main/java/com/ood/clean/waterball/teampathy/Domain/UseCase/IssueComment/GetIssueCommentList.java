package com.ood.clean.waterball.teampathy.Domain.UseCase.IssueComment;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.IssueScope;
import com.ood.clean.waterball.teampathy.Domain.Model.IssueComment;
import com.ood.clean.waterball.teampathy.Domain.Repository.IssueCommentRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObserverFactory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@IssueScope
public class GetIssueCommentList extends UseCase<IssueComment, Integer> {

    private IssueCommentRepository issueCommentRepository;

    @Inject
    public GetIssueCommentList(ThreadingObserverFactory threadingObserverFactory, IssueCommentRepository issueCommentRepository) {
        super(threadingObserverFactory);
        this.issueCommentRepository = issueCommentRepository;
    }

    @Override
    protected Observable<IssueComment> buildUseCaseObservable(final Integer page) {
        return Observable.create(new ObservableOnSubscribe<IssueComment>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<IssueComment> e) throws Exception {
                List<IssueComment> issueCommentList = issueCommentRepository.readList(page);
                for ( IssueComment issueComment : issueCommentList )
                    e.onNext(issueComment);
                e.onComplete();
            }
        });
    }
}
