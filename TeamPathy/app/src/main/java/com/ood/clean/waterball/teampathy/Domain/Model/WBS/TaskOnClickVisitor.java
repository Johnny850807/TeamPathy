package com.ood.clean.waterball.teampathy.Domain.Model.WBS;


public interface TaskOnClickVisitor {

	public void taskViewOnClick(TaskGroup TaskGoup);

	public void taskViewOnClick(TodoTask task);

	public void taskViewOnLongClick(TaskItem taskItem);

}
