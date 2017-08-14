package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs.GetMemberTodoList;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.TodoListPresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

@ProjectScope
public class TodolistPresenterImp implements TodoListPresenter {
    private TodoListPresenter.TodoListView todoListView;
    private GetMemberTodoList getMemberTodoList;
    private Member member;

    @Inject
    public TodolistPresenterImp(GetMemberTodoList getMemberTodoList, Member member) {
        this.getMemberTodoList = getMemberTodoList;
        this.member = member;
    }

    public void setTodoListView(TodoListPresenter.TodoListView todoListView) {
        this.todoListView = todoListView;
    }

    @Override
    public void loadTodoList() {
        getMemberTodoList.execute(new DefaultObserver<TodoTask>() {
            @Override
            public void onNext(@NonNull TodoTask todoTask) {
                todoListView.loadTodoTask(todoTask);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                todoListView.onLoadFinishNotify();
            }
        },member);
    }

    @Override
    public void commitTask(TodoTask todoTask) {

    }

    @Override
    public void setAsDoingTask(TodoTask todoTask) {

    }

    @Override
    public void cancelCommit(TodoTask todoTask) {

    }

    @Override
    public void onResume() {}

    @Override
    public void onDestroy() {
        getMemberTodoList.dispose();
    }

}
