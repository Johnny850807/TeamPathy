package com.ood.clean.waterball.teampathy.Domain.Model.WBS;

public class TaskRoot extends TaskGroup {
	public static final String ROOT_SIGN = "--";

	public TaskRoot(String name) {
		super(name, ROOT_SIGN);
		root = this;
        degree = 0;
	}

	@Override
	public void addTaskChild(TaskItem taskItem) {
		taskItem.setDegree(getDegree() + 1);
        //todo get root algorithm is not correct yet, because the order the item is added starts from the leaf node when the leaf node doesn't assign the root value.

		taskItem.setRoot(this);
		taskList.add(taskItem);
	}

	@Override
	public TaskItem getRoot() {
		return this;
	}

	@Override
	public String getOfGroupName() {
		return "--";
	}

    @Override
    public int getDegree() {
        return 0;
    }
}
