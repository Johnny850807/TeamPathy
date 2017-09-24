package com.ood.clean.waterball.teampathy.Presentation.Interfaces;

import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;



public interface WbsConsolePresenter extends LifetimePresenter{

    void loadTasks();
    void executeCommand(WbsCommand wbsCommand);

    public interface WbsView{
        public void onLoadTasksFinish(TaskItem taskroot);
        public void onUpdateTasksFinish(TaskItem taskRoot);
        public void onError(Throwable err);
    }
}
