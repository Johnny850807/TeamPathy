package com.ood.clean.waterball.teampathy.Domain.Model;


import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;

import java.util.List;

public class ProjectProgressInfo {
    private int sum;
    private int done;
    private int notyet;
    private int notReviewed;
    private int notAssigned;

    public ProjectProgressInfo(List<TaskItem> taskItemList){
        for (TaskItem task : taskItemList)
        {
            if (!task.hasChild())
            {
                sum ++;
                switch (task.getStatus())
                {
                    case none:
                        notAssigned ++;
                        notyet ++;
                        break;
                    case pending:
                        notReviewed ++;
                        notyet ++;
                        break;
                    case pass:
                        done ++;
                        break;
                    default:  //assigned, doing
                        notyet ++;
                }
            }
        }

    }

    public ProjectProgressInfo(int sum, int done, int notyet, int notReviewed, int notAssigned) {
        this.sum = sum;
        this.done = done;
        this.notyet = notyet;
        this.notReviewed = notReviewed;
        this.notAssigned = notAssigned;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getNotyet() {
        return notyet;
    }

    public void setNotyet(int notyet) {
        this.notyet = notyet;
    }

    public int getNotReviewed() {
        return notReviewed;
    }

    public void setNotReviewed(int notReviewed) {
        this.notReviewed = notReviewed;
    }

    public int getNotAssigned() {
        return notAssigned;
    }

    public void setNotAssigned(int notAssigned) {
        this.notAssigned = notAssigned;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
