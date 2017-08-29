package com.ood.clean.waterball.teampathy.Domain.Model.WBS;


public class WbsCommand<Data extends TaskItem> {
    private Action action;
    private String parent;
    private String name;
    private Type type;
    private Data data;

    public WbsCommand(Action action, String parent, String name, Type type, Data data) {
        this.action = action;
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public static <Data extends TaskItem> WbsCommand<Data> createTaskChild(String parentName, Data data){
        Type type = getType(data);
        return new WbsCommand<Data>(Action.create, parentName, data.getName(), type, data);
    }

    public static <Data extends TaskItem> WbsCommand<Data> updateTaskItem(String originalName, Data data){
        Type type = getType(data);
        return new WbsCommand<Data>(Action.update, "", originalName, type, data);
    }

    public static <Data extends TaskItem> WbsCommand<Data> removeTaskItem(Data data){
        Type type = getType(data);
        return new WbsCommand<Data>(Action.remove, "", data.getName(), type, data);
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
