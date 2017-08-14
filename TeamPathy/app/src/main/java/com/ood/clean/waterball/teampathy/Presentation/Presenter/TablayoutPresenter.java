package com.ood.clean.waterball.teampathy.Presentation.Presenter;


import com.google.firebase.messaging.FirebaseMessaging;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;

public class TablayoutPresenter {

    private Project project;
    private User user;

    public void registerToProjectFirebaseTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic(projectToTopic());
    }


    private String projectToTopic(){
        return String.format("%d %s -- topic", project.getId(), project.getName());
    }
}
