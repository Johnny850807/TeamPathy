package com.ood.clean.waterball.teampathy.Domain.Model.Member;

import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;



public class MemberIdCard {
    private Member member;
    private TodoTask todoTask;

    public MemberIdCard(){}

    public MemberIdCard(Member member, TodoTask todoTask){
        this.member = member;
        this.todoTask = todoTask;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public TodoTask getTodoTask() {
        return todoTask;
    }

    public void setTodoTask(TodoTask todoTask) {
        this.todoTask = todoTask;
    }

    public Position getPosition(){
        return member.getMemberDetails().getPosition();
    }
}