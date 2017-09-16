package com.ood.clean.waterball.teampathy.Domain.DI.Component;

import com.ood.clean.waterball.teampathy.Domain.DI.Module.WbsModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Scope.WbsScope;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.AssignTaskDialogFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateTaskGroupDialogFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateTodoTaskDialogFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Fragment.ChartWebViewFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Fragment.TaskPendingFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Fragment.WbsConsoleFragment;

import dagger.Subcomponent;


@WbsScope
@Subcomponent(
        modules = WbsModule.class
)
public interface WbsComponent {
    void inject(WbsConsoleFragment wbsConsoleFragment);
    void inject(ChartWebViewFragment chartWebViewFragment);
    void inject(TaskPendingFragment taskPendingFragment);

    void inject(CreateTodoTaskDialogFragment createTodoTaskDialogFragment);
    void inject(CreateTaskGroupDialogFragment createTodoTaskDialogFragment);
    void inject(AssignTaskDialogFragment assignTaskDialogFragment);
}
