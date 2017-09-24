package com.ood.clean.waterball.teampathy.Presentation.Interfaces;


import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;

public interface TodoListPresenter extends LifetimePresenter{

    public interface TodoListView{
        public void loadTodoTask(TodoTask todoTask);
        public void onLoadFinishNotify();
        public void onAlterFinishNotify(TaskItem todoTask, TodoTask.Status status);
        public void onOperationError(Throwable err);
    }

    void loadTodoList();
    void alterTaskStatus(final TodoTask todoTask, final TodoTask.Status status);
}
