package com.ood.clean.waterball.teampathy.Domain.Repository;


import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;

public interface WbsRepository {
     String getWbs() throws Exception;
     TaskItem updateWbs(TaskItem taskRoot) throws Exception;
     TaskItem getTaskTree() throws Exception;
}
