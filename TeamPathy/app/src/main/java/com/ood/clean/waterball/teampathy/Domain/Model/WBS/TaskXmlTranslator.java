package com.ood.clean.waterball.teampathy.Domain.Model.WBS;

public interface TaskXmlTranslator {

	public String taskToXml(TaskItem taskItem) throws Exception;

	public TaskItem xmlToTasks(String xml) throws Exception;

}
