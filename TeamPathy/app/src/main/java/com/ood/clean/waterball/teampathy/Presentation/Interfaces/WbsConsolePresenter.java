package com.ood.clean.waterball.teampathy.Presentation.Interfaces;

import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;

/**
 * Created by User on 2017/7/19.
 */

public interface WbsConsolePresenter extends LifetimePresenter{

    void loadTasks();
    void updateTasks(TaskItem taskRoot);

    public interface WbsView{
        public void onLoadTasksFinish(TaskItem taskroot);
        public void onUpdateTasksFinish(TaskItem taskRoot);
        public void onError(Exception err);
    }
}
