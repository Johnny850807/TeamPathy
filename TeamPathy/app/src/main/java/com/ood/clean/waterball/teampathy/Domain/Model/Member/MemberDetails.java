package com.ood.clean.waterball.teampathy.Domain.Model.Member;


import java.io.Serializable;

public class MemberDetails implements Serializable{
    private Position position;
    private int contribution;

    public MemberDetails(Position position, int contribution) {
        this.position = position;
        this.contribution = contribution;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPosition(String position) {
        for ( Position p : Position.values() )
            if (p.toString().contains(position))
                this.position = p;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }
}
