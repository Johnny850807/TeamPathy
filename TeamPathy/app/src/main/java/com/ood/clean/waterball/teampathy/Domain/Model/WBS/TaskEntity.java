package com.ood.clean.waterball.teampathy.Domain.Model.WBS;


import java.util.Iterator;


public abstract class TaskEntity implements TaskItem{
    protected int degree = 0;  //todo correct degree algorithm
    protected TaskItem root;
    protected String name;
    protected String parent;


    public TaskEntity(String name, String parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + (hasChild() ? 1231 : 1237);
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaskEntity other = (TaskEntity) obj;
        if (getName() == null) {
            if (other.getName() != null)
                return false;
        } else if (!getName().equals(other.getName()))
            return false;
        if (hasChild() != other.hasChild())
            return false;
        return true;
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
