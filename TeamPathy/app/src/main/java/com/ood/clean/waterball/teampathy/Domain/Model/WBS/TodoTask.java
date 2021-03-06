package com.ood.clean.waterball.teampathy.Domain.Model.WBS;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.ood.clean.waterball.teampathy.MyUtils.EnglishAbbrDateConverter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ood.clean.waterball.teampathy.MyUtils.EnglishAbbrDateConverter.dateToTime;

public class TodoTask extends TaskEntity implements TaskItem, Cloneable, Comparable<TodoTask>, Parcelable{
    public static int UNASSIGNED_ID = -1;
    private int assignedId;
    private String assignedUserImageUrl;
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
                    int assignedId,
                    String assignedUserImageUrl) {
        super(name, ofGroupName);
        this.description = description;
        this.status = status;
        this.contribution = contribution;
        this.dependency = dependency;
        this.assignedId = assignedId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assignedUserImageUrl = assignedUserImageUrl;
    }

    //Default start and end date => today
    public TodoTask(String name,
                    String ofGroupName,
                    String description,
                    int contribution,
                    String dependency,
                    Status status,
                    int assignedId,
                    String assignedUserImageUrl) {
        this(name, ofGroupName, description, contribution, new Date(), new Date(), dependency, status, assignedId, assignedUserImageUrl);
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
        element.setAttribute(TaskXmlTranslatorImp.ASSIGNED_ID_ATT, String.valueOf(assignedId));
        element.setAttribute(TaskXmlTranslatorImp.ASSIGNED_USER_IMAGEURL_ATT, String.valueOf(assignedUserImageUrl));
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
        return EnglishAbbrDateConverter.dateToTime(startDate,false);
    }

    public String getEndDateString(){
        return EnglishAbbrDateConverter.dateToTime(endDate,false);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getParent() {
        return parent;
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

    public int getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(int assignedId) {
        this.assignedId = assignedId;
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
    public String getAssignedUserImageUrl() {
        return assignedUserImageUrl;
    }

    @Override
    public void setAssignedUserImageUrl(String assignedUserImageUrl) {
        this.assignedUserImageUrl = assignedUserImageUrl;
    }

    @Override
    public void acceptEventVisitor(TaskEventVisitor visitor) {
        visitor.eventOnTask(this);
    }

    public TodoTask clone() throws CloneNotSupportedException {
        return (TodoTask)super.clone();
    }


    @Override
    public int compareTo(@NonNull TodoTask todoTask) {
        // doing => none => assigned => pending => pass should be the order in the list
        if (this.getStatus().ordinal() != todoTask.getStatus().ordinal())
            return this.getStatus().ordinal() - todoTask.getStatus().ordinal();

        return this.endDate.compareTo(todoTask.getEndDate()); // sort by the end date, one have the nearer end date should be te front.
    }

    public enum Status{
        doing("doing"),  assigned("assigned"), none("none"),  pending("pending"), pass("pass");
        private String attr;
        private Status(String attr) {
            this.attr = attr;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeSerializable(getParent());
        parcel.writeString(description);
        parcel.writeInt(contribution);
        parcel.writeSerializable(startDate);
        parcel.writeSerializable(endDate);
        parcel.writeString(status.toString());
        parcel.writeInt(assignedId);
        parcel.writeString(assignedUserImageUrl);
    }

    public static final Parcelable.Creator<TodoTask> CREATOR = new Creator<TodoTask>() {
        @Override
        public TodoTask createFromParcel(Parcel parcel) {
            String name = parcel.readString();
            String ofGroup = parcel.readString();
            String description = parcel.readString();
            int contribution = parcel.readInt();
            Date startDate = (Date) parcel.readSerializable();
            Date endDate = (Date) parcel.readSerializable();
            String dependency = parcel.readString();
            Status status = Status.valueOf(parcel.readString());
            int assignedId = parcel.readInt();
            String assignedUserImageUrl = parcel.readString();
            return new TodoTask(name, ofGroup, description, contribution,
                    startDate, endDate, dependency, status, assignedId, assignedUserImageUrl);
        }

        @Override
        public TodoTask[] newArray(int i) {
            return new TodoTask[i];
        }
    };

}
