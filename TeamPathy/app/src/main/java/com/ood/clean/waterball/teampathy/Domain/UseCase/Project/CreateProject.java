package com.ood.clean.waterball.teampathy.Domain.UseCase.Project;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.UserScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Repository.ProjectRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@UserScope
public class CreateProject extends UseCase<Project,Project> {
    private ProjectRepository projectRepository;

    @Inject
    public CreateProject(ThreadingObservableFactory threadingObservableFactory, ProjectRepository projectRepository) {
        super(threadingObservableFactory);
        this.projectRepository = projectRepository;
    }

    @Override
    protected Observable<Project> buildUseCaseObservable(final Project project) {
        return Observable.create(new ObservableOnSubscribe<Project>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Project> e) throws Exception {
                e.onNext(projectRepository.create(project));
                e.onComplete();
            }
        });
    }
}
