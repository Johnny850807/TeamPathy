package com.ood.clean.waterball.teampathy.Domain.Model.Member;

import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;



public class MemberIdCard {
    private Member member;
    private TodoTask dointTask;
    private RewardRecord[] records;

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

    public int getUserId(){
        return member.getUserId();
    }

    public boolean isRecordEmpty(){
        return records.length == 0;
    }

    public RewardRecord[] getRecords() {
        return records;
    }

    public void setRecords(RewardRecord[] records) {
        this.records = records;
    }
}