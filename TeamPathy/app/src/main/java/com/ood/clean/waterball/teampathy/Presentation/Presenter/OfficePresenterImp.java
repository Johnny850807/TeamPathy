package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Position;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Office.BootMember;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Office.ChangeMemberPosition;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Office.GetMemberCardList;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Office.LeaderHandover;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.OfficePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;



@ProjectScope
public class OfficePresenterImp implements OfficePresenter {
    private OfficeView officeView;
    @Inject GetMemberCardList getMemberCardList;
    @Inject ChangeMemberPosition changeMemberPosition;
    @Inject LeaderHandover leaderHandover;
    @Inject BootMember bootMember;
    @Inject Project project;
    @Inject Member currentUser;

    @Inject
    public OfficePresenterImp() {}

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
    public void changeMemberPosition(Member member, Position position) {
        changeMemberPosition.execute(new DefaultObserver<Member>() {
            @Override
            public void onNext(@NonNull Member member) {
                officeView.onMemberInfoUpdated(member);
            }
        }, new ChangeMemberPosition.Params(member, position));
    }

    @Override
    public void leaderHandover(Member member) {
        leaderHandover.execute(new DefaultObserver<Member>() {
            @Override
            public void onNext(@NonNull Member newLeader) {
                // the project can have only one leader, so if the handover succeeds, the old leader should become to a manager.
                currentUser.getMemberDetails().setPosition(Position.manager);
                officeView.onMemberInfoUpdated(currentUser); // update the old leader to manager
                officeView.onMemberInfoUpdated(newLeader); // update the new leader

            }
        }, member);
    }

    @Override
    public void bootMember(final Member member) {
        bootMember.execute(new DefaultObserver<Void>() {
            @Override
            public void onNext(@NonNull Void aVoid) {}

            @Override
            public void onComplete() {
                officeView.onMemberBootedCompleted(member);
            }
        }, member);
    }

    @Override
    public void onResume() {}

    @Override
    public void onDestroy() {
        getMemberCardList.dispose();
        changeMemberPosition.dispose();
        leaderHandover.dispose();
        bootMember.dispose();
    }

}
