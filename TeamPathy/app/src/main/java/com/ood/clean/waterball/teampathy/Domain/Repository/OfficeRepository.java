package com.ood.clean.waterball.teampathy.Domain.Repository;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberDetails;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Position;

import java.util.List;



public interface OfficeRepository{

    List<MemberIdCard> getMemberIdCardList() throws Exception;

    MemberDetails getMemberDetails(int userId) throws Exception;

    MemberDetails changeMemberPosition(int userId, Position position) throws Exception;

    void evictMember(int userId) throws Exception;

    void notifyAllMembers(String content) throws Exception;



}
