package com.ood.clean.waterball.teampathy.Presentation.Interfaces;


import com.ood.clean.waterball.teampathy.Domain.Model.ReviewTaskCard;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;

public interface ReviewTaskPresenter extends LifetimePresenter{
    void loadReviewTaskCard();
    void rejectTodotask(TodoTask todoTask);
    void reviewAsPassTodoTask(TodoTask todoTask);
    public interface ReviewTaskView{
        void onLoadReviewTaskCard(ReviewTaskCard card);
        void onReviewTasksLoadComplete();
        void onOperationError(Throwable err);
        void onTaskReviewOperationSuccessfully(TodoTask todoTask);
    }
}
