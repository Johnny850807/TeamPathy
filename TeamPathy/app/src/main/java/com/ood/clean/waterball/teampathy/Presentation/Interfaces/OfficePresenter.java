package com.ood.clean.waterball.teampathy.Presentation.Interfaces;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;

/**
 * Created by User on 2017/7/19.
 */

public interface OfficePresenter extends LifetimePresenter {

    public interface OfficeView{
        void loadMemberCard(MemberIdCard memberIdCard);
        void onLoadFinish();
        void onOperationTimeout(Throwable err);
        //todo office functionalities
    }

    void loadAllMemberCards();
}
