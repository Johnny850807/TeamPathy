package com.ood.clean.waterball.teampathy.Presentation.Interfaces;


import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;

public interface TodoListPresenter extends LifetimePresenter{

    public interface TodoListView{
        public void loadTodoTask(TodoTask todoTask);
        public void onLoadFinishNotify();
    }

    void loadTodoList();
    void commitTask(TodoTask todoTask);
    void setAsDoingTask(TodoTask todoTask);
    void cancelCommit(TodoTask todoTask);
}
