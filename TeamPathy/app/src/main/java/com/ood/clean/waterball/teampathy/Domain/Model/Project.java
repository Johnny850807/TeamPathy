package com.ood.clean.waterball.teampathy.Domain.Model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
* The entity presents in the User main page where shows all the projects user owns.
 */
public class Project extends BaseEntity {
    public final static String NO_PASSWORD = "";
    private List<String> issueCategoryList;
    private String name;
    private String description;
    private String category;
    private String imageUrl;
    private String password = NO_PASSWORD;
    private boolean hasPassword = false;

    public Project(){}

    public Project(String name, String category, String description, String imageUrl, String password){
        this.name = name;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
        this.password = password;
        hasPassword = !(password == null || password.isEmpty());
        issueCategoryList = new ArrayList<>();
    }

    public Project(String name, String category, String description, String imageUrl) {
        this(name, category,description,imageUrl,NO_PASSWORD);
    }

    public Map<String,String> toFieldMap(){
        Map<String,String> fields = new HashMap<>();
        fields.put("id", String.valueOf(id));
        fields.put("name", name);
        fields.put("description", description);
        fields.put("category", category);
        if (!(imageUrl == null) && !(imageUrl.isEmpty()))
            fields.put("imageUrl", imageUrl);
        fields.put("password", password);
        return fields;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean hasPassword(){
        return hasPassword;
    }

    public List<String> getIssueCategoryList() {
        return issueCategoryList;
    }

    public void setIssueCategoryList(List<String> issueCategoryList) {
        this.issueCategoryList = issueCategoryList;
    }

    @Override
    public String toString() {
        return String.format("Project name : %s , description : %s" , name , description);
    }

}
