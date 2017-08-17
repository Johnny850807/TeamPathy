package com.ood.clean.waterball.teampathy.Domain.Model;


import android.support.annotation.NonNull;

import com.ood.clean.waterball.teampathy.MyUtils.EnglishAbbrDateConvert;

import java.util.Date;

/**
 * The base entity which needs to store a date.
 */
public class PostDateEntity extends BaseEntity implements Comparable<PostDateEntity>{

    protected Date postDate = new Date();

    public PostDateEntity(){}

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getDateString(){
        return EnglishAbbrDateConvert.dateToTime(postDate,false);
    }

    public String getDateString(boolean showTime){
        return EnglishAbbrDateConvert.dateToTime(postDate,showTime);
    }

    @Override
    public int compareTo(@NonNull PostDateEntity entity) {
        if (postDate.after(entity.getPostDate()))
            return -1;
        if (postDate.equals(entity.getPostDate()))
            return 0;
        return 1;
    }
}
