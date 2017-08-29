package com.ood.clean.waterball.teampathy.Domain.DI.Component;

import com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit.RetrofitIssueModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit.RetrofitProjectModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Module.WbsModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateIssueDialogFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Fragment.IssuesFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Fragment.OfficeFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Fragment.TabLayoutFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Fragment.TaskAnalyticsFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Fragment.TimelinesFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Fragment.TodolistFragment;

import dagger.Subcomponent;


@ProjectScope
@Subcomponent(
        modules = RetrofitProjectModule.class
)
public interface ProjectComponent {
    IssueComponent plus(RetrofitIssueModule issueModule);
    WbsComponent plus(WbsModule wbsModule);

    void inject(TabLayoutFragment tablayoutFragment);
    void inject(TimelinesFragment timelinesFragment);
    void inject(IssuesFragment issuesFragment);
    void inject(TodolistFragment todolistFragment);
    void inject(OfficeFragment officeFragment);
    void inject(TaskAnalyticsFragment taskAnalyticsFragment);

    void inject(CreateIssueDialogFragment createIssueDialogFragment);
}
