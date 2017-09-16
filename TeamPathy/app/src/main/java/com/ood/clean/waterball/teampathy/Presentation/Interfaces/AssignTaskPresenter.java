package com.ood.clean.waterball.teampathy.Presentation.Interfaces;


import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;

public interface AssignTaskPresenter {
    void loadMemberCard();

    interface AssignTaskView{
        void onLoadMember(MemberIdCard member);
    }
}
