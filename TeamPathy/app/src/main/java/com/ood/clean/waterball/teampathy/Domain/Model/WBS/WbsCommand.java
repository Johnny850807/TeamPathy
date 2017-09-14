package com.ood.clean.waterball.teampathy.Domain.Model.WBS;


import java.util.Date;

public class WbsCommand {
    private Action action;
    private String parent;
    private String name;
    private Type type;
    private Data data;

    public WbsCommand(Action action, String parent, String name, Type type, TaskItem data) {
        this.action = action;
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.data = parseData(data);
    }

    private Data parseData(TaskItem data){
        return new Data(data.getName(), data.getAssignedUserId(), data.getDescription(),
                data.getStartDate(), data.getEndDate(), data.getContribution(), data.getDependency(), data.getStatus());
    }

    public static WbsCommand createTaskChild(String parentName, TaskItem data){
        Type type = getType(data);
        return new WbsCommand(Action.create, parentName, data.getName(), type, data);
    }

    public static WbsCommand updateTaskItem(String originalName,TaskItem data){
        Type type = getType(data);
        return new WbsCommand(Action.update, "", originalName, type, data);
    }

    public static WbsCommand removeTaskItem(TaskItem data){
        Type type = getType(data);
        return new WbsCommand(Action.remove, "", data.getName(), type, data);
    }

    private static <T extends TaskItem> Type getType(T data){
        return data instanceof TaskGroup ? Type.TaskGroup : Type.Task;
    }

    public enum Action{
        create, remove, update
    }

    public enum Type{
        Task, TaskGroup
    }

    public class Data{
        private String name;
        private int assignedUserId;
        private String description;
        private Date startDate;
        private Date endDate;
        private int contribution;
        private String dependency;
        private TodoTask.Status status;

        public Data(String name, int assignedUserId, String description, Date startDate, Date endDate, int contribution, String dependency, TodoTask.Status status) {
            this.name = name;
            this.assignedUserId = assignedUserId;
            this.description = description;
            this.startDate = startDate;
            this.endDate = endDate;
            this.contribution = contribution;
            this.dependency = dependency;
            this.status = status;
        }

        public int getAssignedUserId() {
            return assignedUserId;
        }

        public void setAssignedUserId(int assignedUserId) {
            this.assignedUserId = assignedUserId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public int getContribution() {
            return contribution;
        }

        public void setContribution(int contribution) {
            this.contribution = contribution;
        }

        public String getDependency() {
            return dependency;
        }

        public void setDependency(String dependency) {
            this.dependency = dependency;
        }

        public TodoTask.Status getStatus() {
            return status;
        }

        public void setStatus(TodoTask.Status status) {
            this.status = status;
        }
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
