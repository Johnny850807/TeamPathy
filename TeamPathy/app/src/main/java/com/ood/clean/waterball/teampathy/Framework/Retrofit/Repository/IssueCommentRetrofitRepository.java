package com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.IssueScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory.ExceptionConverter;
import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.Domain.Model.IssueComment;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.IssueCommentRepository;

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

@IssueScope
public class IssueCommentRetrofitRepository implements IssueCommentRepository {
    private ExceptionConverter exceptionConverter;
    private User user;
    private Project project;
    private Issue issue;
    private IssueCommentApi issueCommentApi;

    @Inject
    public IssueCommentRetrofitRepository(ExceptionConverter exceptionConverter,
                                          User user,
                                          Project project,
                                          Issue issue,
                                          Retrofit retrofit) {
        this.exceptionConverter = exceptionConverter;
        this.user = user;
        this.project = project;
        this.issue = issue;

        issueCommentApi = retrofit.create(IssueCommentApi.class);
    }

    @Override
    public IssueComment create(IssueComment entity) throws Exception {
        ResponseModel<IssueComment> response = issueCommentApi.create(project.getId(),
                issue.getId(), user.getId(), entity.toFieldMap()).execute().body();

        if (!exceptionConverter.isSuccessful(response))
            throw exceptionConverter.convert(response);

        return response.getData();
    }

    @Override
    public IssueComment update(IssueComment entity) throws Exception {
        return null;
    }

    @Override
    public IssueComment delete(IssueComment entity) throws Exception {
        return null;
    }

    @Override
    public List<IssueComment> readList(int page) throws Exception {
        ResponseModel<List<IssueComment>> response = issueCommentApi.getList(project.getId(),
                issue.getId(), page).execute().body();

        if (!exceptionConverter.isSuccessful(response))
            throw exceptionConverter.convert(response);

        return response.getData();
    }

    @Override
    public IssueComment readDetails(int id) throws Exception {
        return null;
    }

    interface IssueCommentApi {
        String RESOURCE = "projects/{projectId}/issues/{issueId}/comments";

        @FormUrlEncoded
        @POST(RESOURCE)
        public Call<ResponseModel<IssueComment>> create(@Path("projectId") int projectId,
                                                  @Path("issueId") int issueId,
                                                  @Query("userId") int userId,
                                                  @FieldMap Map<String,String> fields);

        @GET(RESOURCE)
        public Call<ResponseModel<List<IssueComment>>> getList(@Path("projectId") int projectId,
                                                         @Path("issueId") int issueId,
                                                         @Query("page") int page);
        
    }
}
