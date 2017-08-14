package com.ood.clean.waterball.teampathy.Domain.UseCase.Issue;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.Domain.Repository.IssueRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObserverFactory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@ProjectScope
public class GetIssueList extends UseCase<Issue, Integer> {

    private IssueRepository issueRepository;

    @Inject
    public GetIssueList(ThreadingObserverFactory threadingObserverFactory,
                        IssueRepository issueRepository) {
        super(threadingObserverFactory);
        this.issueRepository = issueRepository;
    }

    @Override
    protected Observable<Issue> buildUseCaseObservable(final Integer page) {
        return Observable.create(new ObservableOnSubscribe<Issue>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Issue> e) throws Exception {
                List<Issue> issueList = issueRepository.readList(page);
                for ( Issue issue : issueList )
                    e.onNext(issue);
                e.onComplete();
            }
        });
    }
}
