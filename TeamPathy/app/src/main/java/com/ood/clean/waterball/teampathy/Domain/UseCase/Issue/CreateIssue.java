package com.ood.clean.waterball.teampathy.Domain.UseCase.Issue;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.Domain.Repository.IssueRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@ProjectScope
public class CreateIssue extends UseCase<Issue,Issue> {
    private IssueRepository issueRepository;

    @Inject
    public CreateIssue(ThreadingObservableFactory threadingObservableFactory, IssueRepository issueRepository) {
        super(threadingObservableFactory);
        this.issueRepository = issueRepository;
    }

    @Override
    protected Observable<Issue> buildUseCaseObservable(final Issue issue) {
        return Observable.create(new ObservableOnSubscribe<Issue>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Issue> e) throws Exception {
                e.onNext(issueRepository.create(issue));
                e.onComplete();
            }
        });
    }
}
