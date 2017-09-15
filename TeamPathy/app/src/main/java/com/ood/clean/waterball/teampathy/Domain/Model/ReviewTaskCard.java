package com.ood.clean.waterball.teampathy.Domain.Model;


import android.support.annotation.NonNull;

import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReviewTaskCard implements Comparable<ReviewTaskCard>, Serializable{
    private User commiter;
    private List<TodoTask> pendingTasks = new ArrayList<>();

    public User getCommiter() {
        return commiter;
    }


    public void setCommiter(User commiter) {
        this.commiter = commiter;
    }

    public List<TodoTask> getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(List<TodoTask> pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    @Override
    public int compareTo(@NonNull ReviewTaskCard reviewTaskCard) {
        // compare by the amount of pending tasks
        return this.pendingTasks.size() - reviewTaskCard.getPendingTasks().size();
    }
}
