package com.ood.clean.waterball.teampathy.Domain.Model.Member;



public enum Position {
    member("member") , manager("manager") , leader("leader");

    private String position;

    Position(String position) {
        this.position = position;
    }

    public boolean isMember(){
        return this == member;
    }

    public boolean isLeader(){
        return this == leader;
    }

    public boolean isManager(){
        return this == manager;
    }

    @Override
    public String toString(){
        return position;
    }
}
