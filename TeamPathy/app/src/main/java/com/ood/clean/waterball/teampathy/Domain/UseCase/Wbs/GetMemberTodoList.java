package com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObserverFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@ProjectScope
public class GetMemberTodoList extends UseCase<TodoTask,Member> {
    private WbsRepository wbsRepository;

    @Inject
    public GetMemberTodoList(ThreadingObserverFactory threadingObserverFactory,
                             WbsRepository wbsRepository) {
        super(threadingObserverFactory);
        this.wbsRepository = wbsRepository;
    }

    @Override
    protected Observable<TodoTask> buildUseCaseObservable(final Member member) {
        return Observable.create(new ObservableOnSubscribe<TodoTask>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<TodoTask> e) throws Exception {
                TaskItem taskRoot = wbsRepository.getTaskTree();
                for (TaskItem taskItem : taskRoot)
                    if ( taskItem instanceof TodoTask)
                        if (taskItem.getAssignedUserId() == member.getUser().getId())
                        e.onNext((TodoTask)taskItem);
                e.onComplete();
            }
        });
    }
}
