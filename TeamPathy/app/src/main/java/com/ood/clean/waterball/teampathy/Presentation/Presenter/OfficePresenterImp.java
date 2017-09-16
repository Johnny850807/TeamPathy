package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Office.GetMemberCardList;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.OfficePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;



@ProjectScope
public class OfficePresenterImp implements OfficePresenter {
    private OfficeView officeView;
    private GetMemberCardList getMemberCardList;
    private Project project;

    @Inject
    public OfficePresenterImp(GetMemberCardList getMemberCardList, Project project) {
        this.getMemberCardList = getMemberCardList;
        this.project = project;
    }

    public void setOfficeView(OfficeView officeView) {
        this.officeView = officeView;
    }

    @Override
    public void loadAllMemberCards() {
        getMemberCardList.execute(new DefaultObserver<MemberIdCard>() {
            @Override
            public void onNext(@NonNull MemberIdCard card) {
                officeView.loadMemberCard(card);
            }

            @Override
            public void onComplete() {
                officeView.onLoadFinish();
            }

            @Override
            public void onError(Throwable exception) {
                officeView.onOperationTimeout(exception);
            }
        }, project);
    }

    @Override
    public void onResume() {}

    @Override
    public void onDestroy() {
        getMemberCardList.dispose();
    }

}
