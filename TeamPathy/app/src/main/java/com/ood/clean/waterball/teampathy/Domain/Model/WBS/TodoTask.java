package com.ood.clean.waterball.teampathy.Domain.Model.WBS;


import com.ood.clean.waterball.teampathy.MyUtils.EnglishAbbrDateConvert;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ood.clean.waterball.teampathy.MyUtils.EnglishAbbrDateConvert.dateToTime;

public class TodoTask extends TaskEntity implements TaskItem {
    public static int UNASSIGNED_ID = -1;
    private int assignedUserId;
    private String description;
    private Date startDate;
    private Date endDate;
    private int contribution;
    private String dependency;
    private Status status;  // if the task has been reviewed as passed.

    // specific start and end date
    public TodoTask(String name,
                    String ofGroupName,
                    String description,
                    int contribution,
                    Date startDate,
                    Date endDate,
                    String dependency,
                    Status status,
                    int assignedUserId) {
        super(name, ofGroupName);
        this.description = description;
        this.status = status;
        this.contribution = contribution;
        this.dependency = dependency;
        this.assignedUserId = assignedUserId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    //Default start and end date => today
    public TodoTask(String name,
                    String ofGroupName,
                    String description,
                    int contribution,
                    String dependency,
                    Status status,
                    int assignedUserId) {
        this(name, ofGroupName, description, contribution, new Date(), new Date(), dependency, status, assignedUserId);
    }


    @Override
    public List<TaskItem> toList() {
        List<TaskItem> taskItemList = new ArrayList<>();
        taskItemList.add(this);
        return taskItemList;
    }

    @Override
    public void addTaskChild(TaskItem taskItem) {
        throw new RuntimeException("Task item cannot have child.");
    }

    @Override
    public boolean removeTaskChild(TaskItem taskItem) {
        /* no child*/
        return false;
    }

    @Override
    public Node toXmlNode(Document document) {
        Element element = document.createElement(TaskXmlTranslatorImp.TASK_NAME);
        element.setAttribute(TaskXmlTranslatorImp.NAME_ATT,name);
        element.setAttribute(TaskXmlTranslatorImp.DESCRIPTION_ATT,description);
        element.setAttribute(TaskXmlTranslatorImp.STARTDATE_ATT, dateToTime(startDate, false));
        element.setAttribute(TaskXmlTranslatorImp.ENDDATE_ATT,dateToTime(endDate,false));
        element.setAttribute(TaskXmlTranslatorImp.CONTRIBUTION_ATT, String.valueOf(contribution));
        element.setAttribute(TaskXmlTranslatorImp.STATUS_ATT,status.attr);
        element.setAttribute(TaskXmlTranslatorImp.ASSIGNED_ID_ATT, String.valueOf(assignedUserId));
        return element;
    }

    @Override
    public void setDegree(int degree) {
        this.degree = degree;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStartDateString(){
        return EnglishAbbrDateConvert.dateToTime(startDate,false);
    }

    public String getEndDateString(){
        return EnglishAbbrDateConvert.dateToTime(endDate,false);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOfGroupName() {
        return ofGroupName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getContribution() {
        return contribution;
    }

    @Override
    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    @Override
    public int getAssignedUserId() {
        return assignedUserId;
    }

    @Override
    public void setAssignedUserId(int assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    @Override
    public boolean hasChild() {
        return false;
    }

    @Override
    public void acceptOnEditVisitor(TaskOnEditVisitor visitor) {
        visitor.taskOnEdit(this);
    }

    @Override
    public void acceptOnClickVisitor(TaskOnClickVisitor visitor) {
        visitor.taskViewOnClick(this);
    }

    @Override
    public void acceptOnLongClickVisitor(TaskOnClickVisitor visitor) {
        visitor.taskViewOnLongClick(this);
    }


    public enum Status{
        NONE("none"), ASSIGNED("assigned"), PENDING("pending"), PASS("pass");
        private String attr;
        private Status(String attr) {
            this.attr = attr;
        }
    }
}
