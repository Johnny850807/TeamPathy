package com.ood.clean.waterball.teampathy.Domain.Model.WBS;


public interface TaskEventVisitor {
	public void eventOnTask(final TaskGroup taskGroup);
	public void eventOnTask(final TodoTask task);
}
