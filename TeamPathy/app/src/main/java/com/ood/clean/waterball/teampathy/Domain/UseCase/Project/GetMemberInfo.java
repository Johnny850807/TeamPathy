package com.ood.clean.waterball.teampathy.Domain.UseCase.Project;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.UserScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Repository.ProjectRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@UserScope
public class GetMemberInfo extends UseCase<Member, GetMemberInfo.Params> {

    private ProjectRepository projectRepository;

    @Inject
    public GetMemberInfo(ThreadingObservableFactory threadingObservableFactory, ProjectRepository projectRepository) {
        super(threadingObservableFactory);
        this.projectRepository = projectRepository;
    }

    @Override
    protected Observable<Member> buildUseCaseObservable(final GetMemberInfo.Params params) {
        return Observable.create(new ObservableOnSubscribe<Member>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Member> e) throws Exception {
                e.onNext(projectRepository.getMemberInfo(params));
                e.onComplete();
            }
        });
    }

    public static class Params{

        private int userId;
        private int projectId;

        public Params(int userId, int projectId) {
            this.userId = userId;
            this.projectId = projectId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }
    }
}
