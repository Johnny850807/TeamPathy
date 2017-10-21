package com.ood.clean.waterball.teampathy.Domain.Model.WBS;


import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface TaskItem extends Iterable<TaskItem>, Serializable{
    public int NO_USER_ID = -9999;

    public TaskItem getRoot();

    public void setRoot(TaskItem root);

    public String getAssignedUserImageUrl();

    public void setAssignedUserImageUrl(String assignedUserImageUrl);

    public List<TaskItem> toList();

    public void addTaskChild(TaskItem taskItem);

    public boolean removeTaskChild(TaskItem taskItem);

    public void setAssignedId(int assignedId);

    public int getAssignedId();

    public void setDegree(int degree);

    public int getDegree();

    public String getParent();

    public void setParent(String parent);

    public String getName();

    public void setName(String name);

    public Date getEndDate();

    public void setEndDate(Date endDate);

    public Date getStartDate();

    public void setStartDate(Date startDate);

    public String getDescription();

    public void setDescription(String description);

    public int getContribution();

    public void setContribution(int contribution);

    public TodoTask.Status getStatus();

    public void setStatus(TodoTask.Status status);

    public Node toXmlNode(Document document);

    public String getDependency();

    public void setDependency(String dependency);

    public boolean hasChild();

    //visitor
    public void acceptEventVisitor(TaskEventVisitor visitor);
}
