package com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.UserScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.validator.ExceptionValidator;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.ProjectRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.GetMemberInfo;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.JoinProject;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

@UserScope
public class ProjectRetrofitRepository implements ProjectRepository {
    private ExceptionValidator exceptionValidator;
    private ProjectApi projectApi;
    private User user;

    @Inject
    public ProjectRetrofitRepository(Retrofit retrofit,
                                     ExceptionValidator exceptionValidator, User user) {
        this.exceptionValidator = exceptionValidator;
        this.user = user;

        projectApi = retrofit.create(ProjectApi.class);
    }

    @Override
    public Project create(Project entity) throws Exception {
        ResponseModel<Project> response = projectApi.createProject(user.getId(),
                entity.toFieldMap()).execute().body();

        exceptionValidator.validate(response);

        return response.getData();
    }

    @Override
    public Project update(Project entity) throws Exception {
        //todo
        return null;
    }

    @Override
    public Project delete(Project entity) throws Exception {
        //todo
        return null;
    }

    @Override
    public List<Project> readList(int page) throws Exception {
        //todo
        return null;
    }

    @Override
    public Project readDetails(int id) throws Exception {
        ResponseModel<Project> response =
                projectApi.getProjectDetails(id).execute().body();

        exceptionValidator.validate(response);
        return response.getData();
    }

    @Override
    public Member getMemberInfo(GetMemberInfo.Params params) throws Exception {
        ResponseModel<Member> response = projectApi.getMemberDetails(params.getProjectId(), params.getUserId())
                .execute().body();

        exceptionValidator.validate(response);
        return response.getData();
    }

    @Override
    public List<Project> searchProjectByName(String projectName) throws Exception {
        ResponseModel<List<Project>> response = projectApi.searchProjectByName(user.getId(), projectName, 0)
                .execute().body();

        exceptionValidator.validate(response);
        return response.getData();
    }

    @Override
    public void joinProject(JoinProject.Params params) throws Exception {
        ResponseModel<Void> response =
                projectApi.joinProject(params.getProject().getId(), user.getId() , params.getPassword())
                .execute().body();

        exceptionValidator.validate(response);
    }

    @Override
    public List<Member> getMemberList() throws Exception {
        return null;
    }

    @Override
    public void caseover(Project project) throws Exception {
        ResponseModel<Void> response = projectApi.caseover(project.getId()).execute().body();

        exceptionValidator.validate(response);
    }


    interface ProjectApi{
        String RESOURCE = "projects";

        @FormUrlEncoded
        @POST(RESOURCE)
        public Call<ResponseModel<Project>> createProject(@Query("userId") int userId,
                                                          @FieldMap Map<String,String> fields);

        @GET (RESOURCE + "/{projectId}")
        public Call<ResponseModel<Project>> getProjectDetails(@Path("projectId") int projectId);

        @GET (RESOURCE + "/{projectId}/memberDetails")
        public Call<ResponseModel<Member>> getMemberDetails(@Path("projectId") int projectId,
                                                            @Query("userId") int userId);

        @GET (RESOURCE)
        public Call<ResponseModel<List<Project>>> searchProjectByName(@Query("userId") int userId,
                                                          @Query("name") String name,
                                                          @Query("page") int page);

        @POST (RESOURCE + "/{projectId}/join")
        public Call<ResponseModel<Void>> joinProject(@Path("projectId") int projectId,
                                                     @Query("userId") int userId,
                                                          @Query("password") String password);

        @POST (RESOURCE + "/{projectId}/caseover")
        public Call<ResponseModel<Void>> caseover(@Path("projectId") int projectId);
    }
}
