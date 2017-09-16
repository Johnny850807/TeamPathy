package com.ood.clean.waterball.teampathy.Domain.UseCase.Project;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Repository.ProjectRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@ProjectScope
public class GetMemberList extends UseCase<Member, Project> {
    private ProjectRepository projectRepository;

    @Inject
    public GetMemberList(ThreadingObservableFactory threadingObservableFactory,
                         ProjectRepository projectRepository) {
        super(threadingObservableFactory);
        this.projectRepository = projectRepository;
    }

    @Override
    protected Observable<Member> buildUseCaseObservable(Project project) {
        return Observable.create(new ObservableOnSubscribe<Member>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Member> e) throws Exception {
                List<Member> memberList = projectRepository.getMemberList();
                for (Member member : memberList)
                    e.onNext(member);
                e.onComplete();
            }
        });
    }
}
