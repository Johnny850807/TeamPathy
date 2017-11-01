package com.ood.clean.waterball.teampathy.Domain.UseCase.Project;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Repository.ProjectRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;


@ProjectScope
public class DoProjectCaseOver extends UseCase<Void, Project> {
    private ProjectRepository projectRepository;

    @Inject
    public DoProjectCaseOver(ThreadingObservableFactory threadingObservableFactory, ProjectRepository projectRepository) {
        super(threadingObservableFactory);
        this.projectRepository = projectRepository;
    }

    @Override
    protected Observable<Void> buildUseCaseObservable(final Project project) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> e) throws Exception {
                try{
                    projectRepository.caseover(project);
                    e.onComplete();
                }catch (Exception err){
                    e.onError(err);
                }
            }
        });
    }
}
