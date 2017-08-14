package com.ood.clean.waterball.teampathy.Presentation.Interfaces;


import com.ood.clean.waterball.teampathy.Domain.Model.Project;

public interface SearchProjectView {
    void loadEntity(Project project);
    void onSearchFinish();

    void onProjectPasswordInvalid();
}
