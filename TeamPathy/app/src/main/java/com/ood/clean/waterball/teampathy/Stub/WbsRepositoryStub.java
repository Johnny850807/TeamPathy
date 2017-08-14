package com.ood.clean.waterball.teampathy.Stub;

import android.content.Context;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskXmlTranslator;
import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


@ProjectScope
public class WbsRepositoryStub implements WbsRepository {
    private static Map<Project,String> wbsMap = new HashMap<>();
    private Project project;
    private TaskXmlTranslator taskXmlTranslatorImp;
    private Context context;

    @Inject
    public WbsRepositoryStub(Project project,
                             TaskXmlTranslator taskXmlTranslatorImp,
                             Context context) {
        this.project = project;
        this.taskXmlTranslatorImp = taskXmlTranslatorImp;
        this.context = context;
    }

    @Override
    public String getWbs() throws Exception {
        if (!wbsMap.containsKey(project))
            wbsMap.put(project,AssetXmlStub.getXml(context));
        return wbsMap.get(project);
    }

    @Override
    public TaskItem updateWbs(TaskItem taskRoot) throws Exception {
        String wbs = taskXmlTranslatorImp.taskToXml(taskRoot);
        wbsMap.put(project,wbs);
        return taskRoot;
    }

    @Override
    public TaskItem getTaskTree() throws Exception {
        if (!wbsMap.containsKey(project))
            wbsMap.put(project,AssetXmlStub.getXml(context));
        return taskXmlTranslatorImp.xmlToTasks(wbsMap.get(project));
    }

}
