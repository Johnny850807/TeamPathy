package com.ood.clean.waterball.teampathy.Domain.UseCase.Project;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import javax.inject.Inject;

import io.reactivex.Observable;


@ProjectScope
public class DoProjectCaseOver extends UseCase<Void, Project> {

    @Inject
    public DoProjectCaseOver(ThreadingObservableFactory threadingObservableFactory) {
        super(threadingObservableFactory);
    }

    @Override
    protected Observable<Void> buildUseCaseObservable(Project project) {
        return null;
    }
}
