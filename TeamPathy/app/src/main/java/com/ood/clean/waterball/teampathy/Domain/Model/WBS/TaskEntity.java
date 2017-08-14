package com.ood.clean.waterball.teampathy.Domain.Model.WBS;


import java.util.Iterator;


public abstract class TaskEntity implements TaskItem{
    protected int degree = 0;  //todo correct degree algorithm
    protected TaskItem root;
    protected String name;
    protected String ofGroupName;


    public TaskEntity(String name, String ofGroupName) {
        this.name = name;
        this.ofGroupName = ofGroupName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getOfGroupName() {
        return ofGroupName;
    }

    @Override
    public void setOfGroupName(String ofGroupName) {
        this.ofGroupName = ofGroupName;
    }

    @Override
    public int getDegree() {
        return degree;
    }



    @Override
    public Iterator<TaskItem> iterator() {
        return toList().iterator();
    }

    @Override
    public TaskItem getRoot() {
        return root;
    }

    @Override
    public void setRoot(TaskItem root) {
        this.root = root;
    }

    @Override
    public boolean equals(Object obj) {
        //todo should also compare to the root to check if they are in the same tree
        TaskItem task = (TaskItem) obj;
        return getName().equals(task.getName())
                && (hasChild() == task.hasChild());
    }

    @Override
    public int hashCode() {
        return (getRoot().getName() + getName() + hasChild()).hashCode();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DFS: ");
        for (TaskItem taskItem : this)
            stringBuilder.append(" -> ").append(taskItem.getName()).append("-").append(taskItem.getDegree());
        return stringBuilder.toString();
    }
}
