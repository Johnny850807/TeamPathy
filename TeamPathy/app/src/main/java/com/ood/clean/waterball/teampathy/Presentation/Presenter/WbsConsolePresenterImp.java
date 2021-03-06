package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.WbsScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs.ExecuteWbsCommand;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs.GetWbs;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.WbsConsolePresenter;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

/* wbs presenter cannot have a project scope because the use cases
    will be disposed on destroy and never works again.
*/
@WbsScope
public class WbsConsolePresenterImp implements WbsConsolePresenter {
    @Inject Project project;
    @Inject GetWbs getWbs;
    @Inject ExecuteWbsCommand executeWbsCommand;
    private WbsView wbsView;
    private Map<String, WbsUpdatedListener> wbsUpdatedListeners = new HashMap<>(); // <the listener's name / listener>

    @Inject
    public WbsConsolePresenterImp() {}

    public void setWbsView(WbsView wbsView){
        this.wbsView = wbsView;
    }

    @Override
    public void putWbsUpdatedListener(String name, WbsUpdatedListener wbsUpdatedListener){
        wbsUpdatedListeners.put(name, wbsUpdatedListener);
    }

    private void notifyAllListeners(TaskItem newTaskRoot){
        for (WbsUpdatedListener listener : wbsUpdatedListeners.values())
            listener.onUpdateTasksFinish(newTaskRoot);
    }

    @Override
    public void loadTasks() {
        getWbs.execute(new DefaultObserver<TaskItem>() {
            @Override
            public void onNext(@NonNull TaskItem taskRoot) {
                wbsView.onLoadTasksFinish(taskRoot);
            }
            @Override
            public void onError(Throwable exception) {
                wbsView.onError(exception);
            }
        }, project);
    }

    @Override
    public void executeCommand(WbsCommand wbsCommand) {
        executeWbsCommand.execute(new DefaultObserver<TaskItem>() {
            @Override
            public void onNext(@NonNull TaskItem taskRoot) {
                notifyAllListeners(taskRoot);
            }
            @Override
            public void onError(Throwable exception) {
                wbsView.onError(exception);
            }
        }, wbsCommand);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        getWbs.dispose();
        executeWbsCommand.dispose();
    }

}
