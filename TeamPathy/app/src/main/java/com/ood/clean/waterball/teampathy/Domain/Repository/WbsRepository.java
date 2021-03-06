package com.ood.clean.waterball.teampathy.Domain.Repository;


import com.ood.clean.waterball.teampathy.Domain.Model.ReviewTaskCard;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;

import java.util.List;

public interface WbsRepository {
     String getWbs() throws Exception;
     <Data extends TaskItem> String executeWbsCommand(WbsCommand command) throws Exception;
     List<TodoTask> getTodolist(int userId) throws Exception;
     List<TodoTask> filterTasksByStatus(TodoTask.Status status) throws Exception;
     List<ReviewTaskCard> getReviewTaskCards() throws Exception;
}
