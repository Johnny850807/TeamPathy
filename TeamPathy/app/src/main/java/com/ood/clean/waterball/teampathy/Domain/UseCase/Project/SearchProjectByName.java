package com.ood.clean.waterball.teampathy.Domain.UseCase.Project;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.UserScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Repository.ProjectRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObserverFactory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@UserScope
public class SearchProjectByName extends UseCase<Project, String> {

    private ProjectRepository projectRepository;

    @Inject
    public SearchProjectByName(ThreadingObserverFactory threadingObserverFactory, ProjectRepository projectRepository) {
        super(threadingObserverFactory);
        this.projectRepository = projectRepository;
    }

    @Override
    protected Observable<Project> buildUseCaseObservable(final String projectName) {
        return Observable.create(new ObservableOnSubscribe<Project>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Project> e) throws Exception {
                List<Project> result = projectRepository.searchProjectByName(projectName);
                for( Project project : result )
                    e.onNext(project);
                e.onComplete();
            }
        });
    }

}
