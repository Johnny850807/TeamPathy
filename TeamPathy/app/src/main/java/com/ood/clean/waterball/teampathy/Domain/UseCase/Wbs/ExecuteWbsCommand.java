package com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskXmlTranslator;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;
import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@ProjectScope
public class ExecuteWbsCommand extends UseCase<TaskItem,WbsCommand> {
    private WbsRepository wbsRepository;
    private TaskXmlTranslator translator;

    @Inject
    public ExecuteWbsCommand(ThreadingObservableFactory threadingObservableFactory,
                             WbsRepository wbsRepository, TaskXmlTranslator translator) {
        super(threadingObservableFactory);
        this.wbsRepository = wbsRepository;
        this.translator = translator;
    }

    @Override
    protected Observable<TaskItem> buildUseCaseObservable(final WbsCommand wbsCommand) {
        return Observable.create(new ObservableOnSubscribe<TaskItem>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<TaskItem> e) throws Exception {
                String wbs = wbsRepository.executeWbsCommand(wbsCommand);
                e.onNext(translator.xmlToTasks(wbs));
                e.onComplete();
            }
        });
    }
}
