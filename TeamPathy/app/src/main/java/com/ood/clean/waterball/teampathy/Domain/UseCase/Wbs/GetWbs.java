package com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.WbsScope;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskXmlTranslator;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@WbsScope
public class GetWbs extends UseCase<TaskItem,Project> {
    private WbsRepository wbsRepository;
    private TaskXmlTranslator translator;

    @Inject
    public GetWbs(ThreadingObservableFactory threadingObservableFactory,
                  WbsRepository wbsRepository, TaskXmlTranslator translator) {
        super(threadingObservableFactory);
        this.wbsRepository = wbsRepository;
        this.translator = translator;
    }

    @Override
    protected Observable<TaskItem> buildUseCaseObservable(Project project) {
        return Observable.create(new ObservableOnSubscribe<TaskItem>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<TaskItem> e) throws Exception {
                try{
                    String wbs = wbsRepository.getWbs();
                    e.onNext(translator.xmlToTasks(wbs));
                    e.onComplete();
                }catch (Exception err){
                    err.printStackTrace();
                }
            }
        });
    }
}
