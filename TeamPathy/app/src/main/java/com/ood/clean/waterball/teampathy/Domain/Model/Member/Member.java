package com.ood.clean.waterball.teampathy.Domain.Model.Member;

import com.ood.clean.waterball.teampathy.Domain.Model.User;

/**
 * Member presents the info of how the user participates the project.
 */
public class Member {
    private User user;
    private MemberDetails memberDetails;

    public Member(User user, MemberDetails memberDetails) {
        this.user = user;
        this.memberDetails = memberDetails;
    }

    public static Member create(User user, Position position){
        MemberDetails memberDetails = new MemberDetails(position,0);
        return new Member(user,memberDetails);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isNotManager(){
        return memberDetails.getPosition().isMember();
    }

    public boolean isManager(){
        return !memberDetails.getPosition().isMember();
    }

    public MemberDetails getMemberDetails() {
        return memberDetails;
    }

    public void setMemberDetails(MemberDetails memberDetails) {
        this.memberDetails = memberDetails;
    }
}
