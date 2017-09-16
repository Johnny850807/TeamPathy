package com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory.ExceptionConverter;
import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.IssueRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

@ProjectScope
public class IssueRetrofitRepository implements IssueRepository {
    private ExceptionConverter exceptionConverter;
    private Project project;
    private User user;
    private IssueApi issueApi;

    @Inject
    public IssueRetrofitRepository(Project project,
                                   Retrofit retrofit,
                                   ExceptionConverter exceptionConverter,
                                   User user) {
        this.project = project;
        this.exceptionConverter = exceptionConverter;
        this.user = user;

        issueApi = retrofit.create(IssueApi.class);
    }

    @Override
    public void deleteIssueCategory(String category) throws Exception {
        ResponseModel<Void> response = issueApi.deleteIssueCategory(project.getId(), category, user.getId())
                .execute().body();

        exceptionConverter.validate(response);

        project.getIssueCategoryList().remove(category);
    }

    @Override
    public void addIssueCategory(String category) throws Exception {
        ResponseModel<Void> response = issueApi.addIssueCategory(project.getId(), category, user.getId())
                .execute().body();

        exceptionConverter.validate(response);

        project.getIssueCategoryList().add(category);
    }

    @Override
    public Issue create(Issue entity) throws Exception {
        ResponseModel<Issue> response = issueApi.createIssue(project.getId(),
                user.getId(), entity.toFieldMap()).execute().body();

        exceptionConverter.validate(response);

        return response.getData();
    }

    @Override
    public Issue update(Issue entity) throws Exception {
        return null;  //todo
    }

    @Override
    public Issue delete(Issue entity) throws Exception {
        return null;  //todo
    }

    @Override
    public List<Issue> readList(int page) throws Exception {
        ResponseModel<List<Issue>> response = issueApi.getList(project.getId(), page).execute().body();

        exceptionConverter.validate(response);

        return response.getData();
    }

    @Override
    public Issue readDetails(int id) throws Exception {
        ResponseModel<Issue> response = issueApi.getDetails(project.getId(),
                id).execute().body();

        exceptionConverter.validate(response);

        return response.getData();
    }

    public interface IssueApi{
        String RESOURCE = "projects/{projectId}/issues";

        @FormUrlEncoded
        @POST (RESOURCE)
        public Call<ResponseModel<Issue>> createIssue(@Path("projectId") int projectId,
                                                         @Query("userId") int userId,
                                                         @FieldMap Map<String,String> fields);

        @GET (RESOURCE + "/{issueId}")
        public Call<ResponseModel<Issue>> getDetails(@Path("projectId") int projectId,
                                                            @Path("issueId") int issueId);

        @GET (RESOURCE)
        public Call<ResponseModel<List<Issue>>> getList(@Path("projectId") int projectId,
                                                            @Query("page") int page);

        @POST (RESOURCE + "/categories/{name}")
        public Call<ResponseModel<Void>> addIssueCategory(@Path("projectId") int projectId,
                                                          @Path("name") String name,
                                                          @Query("userId") int userId); //todo remove userId after backend api remove as well


        @DELETE(RESOURCE + "/categories/{name}")
        public Call<ResponseModel<Void>> deleteIssueCategory(@Path("projectId") int projectId,
                                                              @Path("name") String name,
                                                             @Query("userId") int userId);
    }
}
