package com.ood.clean.waterball.teampathy.Domain.Model.WBS;

/**
 * Created by User on 2017/7/31.
 */

public interface TaskOnEditVisitor {
    public void taskOnEdit(TaskGroup taskGroup);
    public void taskOnEdit(TodoTask todoTask);
}
