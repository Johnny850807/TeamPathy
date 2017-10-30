package com.ood.clean.waterball.teampathy.Domain.Model;


import java.util.HashMap;
import java.util.Map;

/**
 * The entity presents in the Timeline section of the project.
 */
public class Timeline extends PostDateEntity {
    private User poster;
    private String content;
    private String complement;
    private String obj;
    private Category category = Category.jotting;

    public Timeline(){}

    public Timeline(User poster, String content) {
        this.content = content;
        this.poster = poster;
    }

    public Map<String,String> toFieldMap(){
        Map<String,String> fields = new HashMap<>();
        fields.put("id", String.valueOf(id));
        fields.put("content", content);
        fields.put("category",category.toString());
        return fields;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String getDateString() {
        return super.getDateString();
    }

    @Override
    public String toString() {
        return poster.getName() + ":" + content;
    }

    public enum Category{
        jotting, notify, task
    }

}
