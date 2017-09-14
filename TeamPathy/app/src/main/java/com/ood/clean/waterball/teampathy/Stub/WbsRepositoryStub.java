package com.ood.clean.waterball.teampathy.Stub;

import android.content.Context;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;
import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


@ProjectScope
public class WbsRepositoryStub implements WbsRepository {
    private static Map<Project,String> wbsMap = new HashMap<>();
    private Project project;
    private Context context;

    @Inject
    public WbsRepositoryStub(Project project,
                             Context context) {
        this.project = project;
        this.context = context;
    }

    @Override
    public String getWbs() throws Exception {
        if (!wbsMap.containsKey(project))
            wbsMap.put(project,AssetXmlStub.getXml(context));
        return wbsMap.get(project);
    }

    @Override
    public String executeWbsCommand(WbsCommand taskRoot) throws Exception {
        return null;
    }

    @Override
    public List<TodoTask> getTodolist(int userId) throws Exception {
        return null;
    }


}
