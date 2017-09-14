package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.WbsScope;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs.GetTaskTree;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs.ExecuteWbsCommand;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.WbsConsolePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

/* wbs presenter cannot have a project scope because the use cases
    will be disposed on destroy and never works again.
*/
@WbsScope
public class WbsConsolePresenterImp implements WbsConsolePresenter {
    private Project project;
    private GetTaskTree getTaskTree;
    private ExecuteWbsCommand executeWbsCommand;
    private WbsView wbsView;

    @Inject
    public WbsConsolePresenterImp(Project project,
                                  GetTaskTree getTaskTree,
                                  ExecuteWbsCommand executeWbsCommand) {
        this.project = project;
        this.getTaskTree = getTaskTree;
        this.executeWbsCommand = executeWbsCommand;
    }

    public void setWbsView(WbsView wbsView){
        this.wbsView = wbsView;
    }


    @Override
    public void loadTasks() {
        getTaskTree.execute(new DefaultObserver<TaskItem>() {
            @Override
            public void onNext(@NonNull TaskItem taskRoot) {
                wbsView.onLoadTasksFinish(taskRoot);
            }
        }, project);
    }

    @Override
    public void executeCommand(WbsCommand wbsCommand) {
        executeWbsCommand.execute(new DefaultObserver<TaskItem>() {
            @Override
            public void onNext(@NonNull TaskItem taskRoot) {
                wbsView.onUpdateTasksFinish(taskRoot);
            }
        }, wbsCommand);
    }


    public void assignTask(TaskItem taskItem, Member assignedMember){
        //todo
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        getTaskTree.dispose();
        executeWbsCommand.dispose();
    }

}
