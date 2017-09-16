package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.WbsScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Office.GetMemberCardList;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.AssignTaskPresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

@WbsScope
public class AssignTaskPresenterImp implements AssignTaskPresenter {
    private AssignTaskView assignTaskView;
    @Inject GetMemberCardList getMemberCardList;
    @Inject Project project;

    @Inject
    public AssignTaskPresenterImp() {}

    public void setAssignTaskView(AssignTaskView assignTaskView) {
        this.assignTaskView = assignTaskView;
    }

    @Override
    public void loadMemberCard() {
        getMemberCardList.execute(new DefaultObserver<MemberIdCard>() {
            @Override
            public void onNext(@NonNull MemberIdCard card) {
                assignTaskView.onLoadMember(card);
            }
        }, project);
    }

}
