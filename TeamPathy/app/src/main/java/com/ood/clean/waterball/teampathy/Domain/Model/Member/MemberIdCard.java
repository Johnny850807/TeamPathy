package com.ood.clean.waterball.teampathy.Domain.Model.Member;

import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;



public class MemberIdCard {
    private Member member;
    private TodoTask dointTask;

    public MemberIdCard(){}

    public MemberIdCard(Member member, TodoTask dointTask){
        this.member = member;
        this.dointTask = dointTask;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public TodoTask getDointTask() {
        return dointTask;
    }

    public void setDointTask(TodoTask dointTask) {
        this.dointTask = dointTask;
    }

    public Position getPosition(){
        return member.getMemberDetails().getPosition();
    }
}