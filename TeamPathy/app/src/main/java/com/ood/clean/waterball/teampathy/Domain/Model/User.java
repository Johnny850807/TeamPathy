package com.ood.clean.waterball.teampathy.Domain.Model;


import com.ood.clean.waterball.teampathy.Domain.DI.Scope.UserScope;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@UserScope
public class User extends BaseEntity {
    private final static String ANONYMOUS_PICTURE = "https://www.fraxa.org/wp-content/uploads/2013/06/anonymous-head-shot.jpg";
    private String imageUrl = ANONYMOUS_PICTURE;
    private String name;
    private int experience;
    private List<Project> projectList = new ArrayList<>();

    @Inject
    public User(){}

    public User(String name){
        this(name, ANONYMOUS_PICTURE);
    }

    public User(String name, String imageUrl){
        this.name = name;
        this.imageUrl = imageUrl;
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

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public void joinProject(Project project){
        getProjectList().add(project);
    }

    public boolean hasJoinedTo(Project project){
        return projectList.contains(project);
    }
    @Override
    public String toString() {
        return name;
    }
}
