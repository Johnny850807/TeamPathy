package com.ood.clean.waterball.teampathy.Domain.Model.Member;



public class RewardRecord {
    private String itemName;
    private int point;

    public RewardRecord(String itemName, int point) {
        this.itemName = itemName;
        this.point = point;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

}
