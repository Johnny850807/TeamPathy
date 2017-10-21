package com.ood.clean.waterball.teampathy.Presentation.Interfaces;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Position;


public interface OfficePresenter extends LifetimePresenter {

    public interface OfficeView{
        void loadMemberCard(MemberIdCard memberIdCard);
        void onMemberInfoUpdated(Member member);
        void onLoadFinish();
        void onOperationTimeout(Throwable err);
        void onMemberBootedCompleted(Member member);
    }

    void loadAllMemberCards();
    void changeMemberPosition(Member member, Position position);
    void leaderHandover(Member member);
    void bootMember(Member member);
}
