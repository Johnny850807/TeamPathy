package com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs;

import android.util.Log;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@ProjectScope
public class GetMemberTodoList extends UseCase<TodoTask,Member> {
    private WbsRepository wbsRepository;

    @Inject
    public GetMemberTodoList(ThreadingObservableFactory threadingObservableFactory,
                             WbsRepository wbsRepository) {
        super(threadingObservableFactory);
        this.wbsRepository = wbsRepository;
    }

    @Override
    protected Observable<TodoTask> buildUseCaseObservable(final Member member) {
        return Observable.create(new ObservableOnSubscribe<TodoTask>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<TodoTask> e) throws Exception {
                List<TodoTask> todoTaskList = wbsRepository.getTodolist(member.getUser().getId());
                for (TodoTask todoTask : todoTaskList)
                {
                    e.onNext(todoTask);
                    Log.d("wbs", "Todotask " + todoTask.getName() + ", hashcode : " + todoTask.hashCode());
                }

                e.onComplete();
            }
        });
    }
}
