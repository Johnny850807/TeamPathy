package com.ood.clean.waterball.teampathy.Presentation.Interfaces;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;



public interface ProjectsPresenter extends CrudPresenter<Project>{
    public interface ProjectView extends CrudPresenter.CrudView<Project>{
        void onMemberInfoLoadFinsih(Project project, Member member);
        void onJoinProjectFinish(Project project);
    }
}
