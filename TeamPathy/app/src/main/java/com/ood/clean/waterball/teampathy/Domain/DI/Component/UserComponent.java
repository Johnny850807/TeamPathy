package com.ood.clean.waterball.teampathy.Domain.DI.Component;

import com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit.RetrofitProjectModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit.RetrofitUserModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Scope.UserScope;
import com.ood.clean.waterball.teampathy.Presentation.UI.Activity.BaseActivity;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateProjectDialogFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.SearchProjectDialogFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Fragment.ProjectsFragment;

import dagger.Subcomponent;



@UserScope
@Subcomponent(
        modules = RetrofitUserModule.class
)
public interface UserComponent {
    ProjectComponent plus(RetrofitProjectModule projectModule);

    void inject(BaseActivity baseActivity);
    void inject(ProjectsFragment projectsFragment);

    void inject(CreateProjectDialogFragment createProjectDialogFragment);
    void inject(SearchProjectDialogFragment searchProjectDialogFragment);
}
