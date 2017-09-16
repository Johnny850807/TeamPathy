package com.ood.clean.waterball.teampathy.Domain.Repository;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.GetMemberInfo;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.JoinProject;

import java.util.List;

public interface ProjectRepository extends Repository<Project> {

     Member getMemberInfo(GetMemberInfo.Params params) throws Exception;

     List<Project> searchProjectByName(String projectName) throws Exception;

     void joinProject(JoinProject.Params params) throws Exception;

     List<Member> getMemberList() throws Exception;
}
