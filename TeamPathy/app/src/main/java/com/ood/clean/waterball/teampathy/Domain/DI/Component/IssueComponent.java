package com.ood.clean.waterball.teampathy.Domain.DI.Component;

import com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit.RetrofitIssueModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Scope.IssueScope;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateIssueCommentDialogFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Fragment.IssueDetailsFragment;

import dagger.Subcomponent;


@IssueScope
@Subcomponent(
        modules = RetrofitIssueModule.class
)
public interface IssueComponent {
    void inject(IssueDetailsFragment issueDetailsFragment);

    void inject(CreateIssueCommentDialogFragment createIssueCommentDialogFragment);
}
