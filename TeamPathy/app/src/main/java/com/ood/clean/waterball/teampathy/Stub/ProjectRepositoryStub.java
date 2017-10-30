package com.ood.clean.waterball.teampathy.Stub;

import android.content.Context;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.UserScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.ProjectPasswordInvalidException;
import com.ood.clean.waterball.teampathy.Domain.Exception.ResourceNotFoundException;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberDetails;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Position;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.ProjectRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.GetMemberInfo;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.JoinProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@UserScope
public class ProjectRepositoryStub implements ProjectRepository {
    public static final String IMAGE_URL_PROJECT = "http://i.imgur.com/fIfxle9.png";
    private User user;
    private Context context;

    @Inject
    public ProjectRepositoryStub(User user, Context context) {
        this.user = user;
        this.context = context;
    }


    @Override
    public Project create(final Project entity) throws InterruptedException {
        Thread.sleep(2000);  //stub
        user.getProjectList().add(entity);
        return entity;
}

    @Override
    public Project update(final Project entity) throws InterruptedException {
        Thread.sleep(2000);  //stub
        user.getProjectList().set(user.getProjectList().indexOf(entity),entity);
        return entity;
    }

    @Override
    public Project delete(final Project entity) throws InterruptedException {
        Thread.sleep(2000);  //stub
        user.getProjectList().remove(entity);
        return entity;
    }

    @Override
    public List<Project> readList(final int page) throws Exception  {
        Thread.sleep(2000);  //stub
        return user.getProjectList();
    }

    @Override
    public Project readDetails(final int id) throws InterruptedException, ResourceNotFoundException, IOException {
        Thread.sleep(2000);  //stub
        for ( Project project : user.getProjectList() )
            return project;
        throw new ResourceNotFoundException();
    }


    @Override
    public Member getMemberInfo(GetMemberInfo.Params params) throws Exception {
        MemberDetails details = new MemberDetails(Position.manager,3500);
        return new Member(user,details);
    }

    @Override
    public List<Project> searchProjectByName(final String projectName) throws InterruptedException {
        Thread.sleep(2000);  //stub
        List<Project> searchSource = new ArrayList<>(user.getProjectList());
        searchSource.addAll(createSearchProjectStubs());
        List<Project> searchResult = new ArrayList<>();
        for ( Project project : searchSource )
            if (project.getName().contains(projectName))
                searchResult.add(project);
        return searchResult;
    }

    private List<Project> createSearchProjectStubs(){
        List<Project> stubs = new ArrayList<>();
        stubs.add(new Project("測試", "軟體專案", "行動化團隊合作系統", "https://8096-presscdn-0-43-pagely.netdna-ssl.com/wp-content/uploads/2014/10/Bentley-Logo.jpg"));
        stubs.add(new Project("密碼123", "軟體專案", "行動化團隊合作系統", "http://www.car-logos.org/wp-content/uploads/2011/09/corvette.png","123"));

        return stubs;
    }


    @Override
    public void joinProject(final JoinProject.Params params) throws InterruptedException, ProjectPasswordInvalidException {
        Thread.sleep(2000);  //stub
        if (!params.getProject().getPassword().equals(params.getPassword())) // just a stub
            throw new ProjectPasswordInvalidException();
        user.joinProject(params.getProject());
    }

    @Override
    public List<Member> getMemberList() throws Exception {
        return null;
    }

    @Override
    public void caseover(Project project) throws Exception {

    }
}
