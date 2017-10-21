package com.ood.clean.waterball.teampathy.Domain.Repository;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Position;

import java.util.List;



public interface OfficeRepository{

    List<MemberIdCard> getMemberIdCardList() throws Exception;

    Member changeMemberPosition(int userId, Position position) throws Exception;

    Member leaderHandover(int memberId) throws Exception;

    void bootMember(int userId) throws Exception;

    void notifyAllMembers(String content) throws Exception;



}
