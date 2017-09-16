package com.ood.clean.waterball.teampathy.Domain.Model.WBS;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TaskGroup extends TaskEntity implements TaskItem {
	protected List<TaskItem> taskList = new ArrayList<>();

	public TaskGroup(String name, String ofGroupName) {
		super(name ,ofGroupName );
	}

	@Override
	public List<TaskItem> toList() {
		List<TaskItem> list = new ArrayList<>();
        list.add(this);
        for ( TaskItem taskItem : taskList )
			list.addAll(taskItem.toList());
		return list;
	}

	@Override
	public void addTaskChild(TaskItem taskItem) {
		taskItem.setDegree(getDegree() + 1);
		taskList.add(taskItem);
		TaskItem root = getRoot();
		taskItem.setRoot(root);
	}

	@Override
	public boolean removeTaskChild(TaskItem taskItem) {
		for ( TaskItem item : taskList )
		{
			if (item.equals(taskItem))
			{
				taskList.remove(taskItem);
				return true;
			}
			else
			{
				boolean foundChild = item.removeTaskChild(taskItem);
                if (foundChild)
					return true;
			}
		}
		return false;
	}


	public void setAssignedId(int assignedId) {
		throw new RuntimeException("Task group does not support setAssignedId()");
	}

	public int getAssignedId() {
		return NO_USER_ID;
	}

	@Override
	public void setDegree(int degree) {
		this.degree = degree;
		for (TaskItem taskItem : taskList)
			taskItem.setDegree(taskItem.getDegree() + degree);
	}

	@Override
	public Date getEndDate() {
		//todo set the end date which the latest end date of child taskList
		return null;
	}

	@Override
	public void setEndDate(Date endDate) {
		throw new RuntimeException("Task group does not support setEndDate()");
	}

	@Override
	public Date getStartDate() {
		//todo set the start date which the earliest start date of child taskList
		return null;
	}

	@Override
	public void setStartDate(Date startDate) {
		throw new RuntimeException("Task group does not support setStartDate()");
	}

	public String getDescription() {
		return name;
	}

	public void setDescription(String description) {
		throw new RuntimeException("Task group does not support setDescription()");
	}

	public int getContribution() {
		int sum = 0;
		for (TaskItem taskItem : taskList)
			sum += taskItem.getContribution();
		return sum;
	}

	public void setContribution(int contribution) {
		throw new RuntimeException("Task group does not support setContribution()");
	}

	@Override
	public TodoTask.Status getStatus() {
		for (TaskItem task : taskList)
			if (task.getStatus() != TodoTask.Status.pass)
				return TodoTask.Status.none;
		return TodoTask.Status.pass;
	}

	@Override
	public void setStatus(TodoTask.Status status) {
		throw new RuntimeException("Task group does not support setStatus()");
	}

	public Node toXmlNode(Document document) {
        Element element = document.createElement(TaskXmlTranslatorImp.TASK_GROUP_NAME);
        element.setAttribute(TaskXmlTranslatorImp.NAME_ATT,name);
        for (TaskItem taskItem : taskList)
            element.appendChild(taskItem.toXmlNode(document));
		return element;
	}

	@Override
	public String getDependency() {
		return "";
	}

	@Override
	public void setDependency(String dependency) {
		throw new RuntimeException("Task group does not support setDependency()");
	}

	public boolean hasChild() {
		return true;
	}

	@Override
	public void acceptEventVisitor(TaskEventVisitor visitor) {
		visitor.eventOnTask(this);
	}


}
