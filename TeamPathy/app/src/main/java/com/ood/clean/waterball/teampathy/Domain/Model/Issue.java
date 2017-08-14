package com.ood.clean.waterball.teampathy.Domain.Model;


import com.ood.clean.waterball.teampathy.Domain.DI.Scope.IssueScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
/*
* The entity presents in the forum section of the project.
 */
@IssueScope
public class Issue extends PostDateEntity {
    private String name;
    private String content;
    private String category;
    private User poster;
    private User lastEditor;
    private List<IssueComment> comments = new ArrayList<>();

    public Issue(){}

    @Inject
    public Issue(User poster, String name, String content, String category){
        this.lastEditor = this.poster = poster;
        this.name = name;
        this.content = content;
        this.category = category;
    }

    public Map<String,String> toFieldMap(){
        Map<String,String> fields = new HashMap<>();
        fields.put("id", String.valueOf(id));
        fields.put("name" , name);
        fields.put("content", content);
        fields.put("category",category);
        return fields;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public User getLastEditor() {
        return lastEditor;
    }

    public void setLastEditor(User lastEditor) {
        this.lastEditor = lastEditor;
    }

    public List<IssueComment> getComments() {
        return comments;
    }

    public void setComments(List<IssueComment> comments) {
        this.comments = comments;
    }

    @Override
    public String getDateString() {
        return super.getDateString();
    }
}
