package com.ood.clean.waterball.teampathy.Presentation.Interfaces;

import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;

public interface TaskPendingPresenter extends LifetimePresenter{

    public interface TaskPendingView {
        public void loadPendingTask(TaskItem pendingTask);
        public void onLoadFinishNotify();
    }
    public void loadAllPendingTasks();
    public void reviewTask(TaskItem taskItem, TodoTask.Status status);
}
