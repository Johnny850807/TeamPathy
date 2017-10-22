package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs.ExecuteWbsCommand;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs.GetMemberTodoList;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.TodoListPresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

public class TodolistPresenterImp implements TodoListPresenter {
    private TodoListPresenter.TodoListView todoListView;
    private GetMemberTodoList getMemberTodoList;
    private ExecuteWbsCommand executeWbsCommand;


    @Inject
    public TodolistPresenterImp(GetMemberTodoList getMemberTodoList,
                                ExecuteWbsCommand executeWbsCommand) {
        this.getMemberTodoList = getMemberTodoList;
        this.executeWbsCommand = executeWbsCommand;
    }

    public void setTodoListView(TodoListPresenter.TodoListView todoListView) {
        this.todoListView = todoListView;
    }

    @Override
    public void loadTodoList(Member member) {
        getMemberTodoList.execute(new DefaultObserver<TodoTask>() {
            @Override
            public void onNext(@NonNull TodoTask todoTask) {
                todoListView.loadTodoTask(todoTask);
            }
            @Override
            public void onComplete() {
                todoListView.onLoadFinishNotify();
            }

            @Override
            public void onError(Throwable exception) {
                todoListView.onOperationError(exception);
            }
        },member);
    }

    @Override
    public void alterTaskStatus(final TodoTask todoTask, final TodoTask.Status status) {
        try {
            final TodoTask clone = todoTask.clone();  // making a data of updated todotask without changing to the real task before done
            clone.setStatus(status);
            WbsCommand wbsCommand = WbsCommand.updateTaskItem(todoTask.getName(), clone);
            executeWbsCommand.execute(new DefaultObserver<TaskItem>() {
                @Override
                public void onNext(@NonNull TaskItem taskRoot) {
                    todoTask.setStatus(status);
                    todoListView.onAlterFinishNotify(todoTask, status);
                }

                @Override
                public void onError(Throwable exception) {
                    todoListView.onOperationError(exception);
                }
            },wbsCommand);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {}

    @Override
    public void onDestroy() {
        getMemberTodoList.dispose();
        executeWbsCommand.dispose();
    }

}
