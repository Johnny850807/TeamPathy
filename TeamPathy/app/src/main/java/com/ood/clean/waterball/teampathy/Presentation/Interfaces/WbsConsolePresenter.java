package com.ood.clean.waterball.teampathy.Presentation.Interfaces;

import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;



public interface WbsConsolePresenter extends LifetimePresenter{

    void loadTasks();
    void executeCommand(WbsCommand wbsCommand);
    void putWbsUpdatedListener(String name, WbsUpdatedListener wbsUpdatedListener);

    public interface WbsView extends WbsUpdatedListener {
        public void onLoadTasksFinish(TaskItem taskroot);
        public void onError(Throwable err);
    }

    public interface WbsUpdatedListener {
        public void onUpdateTasksFinish(TaskItem taskRoot);
    }
}
