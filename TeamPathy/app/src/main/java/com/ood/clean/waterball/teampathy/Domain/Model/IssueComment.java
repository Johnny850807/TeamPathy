package com.ood.clean.waterball.teampathy.Domain.Model;


import java.util.HashMap;
import java.util.Map;
/**
* The entity presents in the Issue in the Forum section of the project.
 */
public class IssueComment extends PostDateEntity {
    private User poster;
    private String content;

    public IssueComment(){}

    public IssueComment(User poster, String content){
        this.poster = poster;
        this.content = content;
    }

    public Map<String,String> toFieldMap(){
        Map<String,String> fields = new HashMap<>();
        fields.put("id", String.valueOf(id));
        fields.put("content", content);
        return fields;
    }

    @Override
    public String getDateString() {
        return super.getDateString();
    }

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
