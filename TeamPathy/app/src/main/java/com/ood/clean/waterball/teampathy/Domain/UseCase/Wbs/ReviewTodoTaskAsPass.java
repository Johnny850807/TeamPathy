package com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs;

import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import io.reactivex.Observable;


public class ReviewTodoTaskAsPass extends UseCase<TodoTask,TodoTask> {


    public ReviewTodoTaskAsPass(ThreadingObservableFactory threadingObservableFactory) {
        super(threadingObservableFactory);
    }

    @Override
    protected Observable<TodoTask> buildUseCaseObservable(TodoTask todoTask) {
        return null;
    }


}
