package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.WbsScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.ReviewTaskCard;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs.ExecuteWbsCommand;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Wbs.GetReviewTaskCards;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.ReviewTaskPresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

@WbsScope
public class ReviewTaskPresenterImp implements ReviewTaskPresenter {
    @Inject GetReviewTaskCards getReviewTaskCards;
    @Inject ExecuteWbsCommand executeWbsCommand;
    @Inject Project project;
    ReviewTaskView reviewTaskView;

    @Inject
    public ReviewTaskPresenterImp(){}

    @Override
    public void loadReviewTaskCard() {
        getReviewTaskCards.execute(new DefaultObserver<ReviewTaskCard>() {
            @Override
            public void onNext(@NonNull ReviewTaskCard card) {
                reviewTaskView.onLoadReviewTaskCard(card);
            }

            @Override
            public void onComplete() {
                reviewTaskView.onReviewTasksLoadComplete();
            }

            @Override
            public void onError(Throwable exception) {
                reviewTaskView.onOperationError(exception);
            }
        }, project);
    }

    @Override
    public void rejectTodotask(final TodoTask todoTask) {
        try {
            TodoTask clone = todoTask.clone();
            clone.setStatus(TodoTask.Status.assigned);
            WbsCommand wbsCommand = WbsCommand.updateTaskItem(todoTask.getName(), todoTask, clone);
            executeWbsCommand.execute(new DefaultObserver<TaskItem>() {
                @Override
                public void onNext(@NonNull TaskItem taskItem) {}

                @Override
                public void onComplete() {
                    todoTask.setStatus(TodoTask.Status.assigned);
                    reviewTaskView.onTaskReviewOperationSuccessfully(todoTask);
                }
            }, wbsCommand);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reviewAsPassTodoTask(final TodoTask todoTask) {
        try{
            TodoTask clone = todoTask.clone();
            clone.setStatus(TodoTask.Status.pass);
            WbsCommand command = WbsCommand.updateTaskItem(todoTask.getName(), todoTask, clone);
            executeWbsCommand.execute(new DefaultObserver<TaskItem>() {
                @Override
                public void onNext(@NonNull TaskItem taskItem) {}
                @Override
                public void onComplete() {
                    todoTask.setStatus(TodoTask.Status.pass);
                    reviewTaskView.onTaskReviewOperationSuccessfully(todoTask);
                }
            }, command);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }


    public void setReviewTaskView(ReviewTaskView reviewTaskView) {
        this.reviewTaskView = reviewTaskView;
    }

    @Override
    public void onResume() {}

    @Override
    public void onDestroy() {
        getReviewTaskCards.dispose();
        executeWbsCommand.dispose();
    }
}
