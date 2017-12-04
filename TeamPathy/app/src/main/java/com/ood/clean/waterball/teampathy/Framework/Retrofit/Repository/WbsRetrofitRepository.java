package com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit.RetrofitHelper;
import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.validator.ExceptionValidator;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.ReviewTaskCard;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;
import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;

import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

@ProjectScope
public class WbsRetrofitRepository implements WbsRepository {
    private User user;
    private Project project;
    private ExceptionValidator exceptionValidator;
    private WbsApi wbsApi;

    @Inject
    public WbsRetrofitRepository(ExceptionValidator exceptionValidator,
                                 Retrofit retrofit,
                                 User user, Project project) {
        this.exceptionValidator = exceptionValidator;
        this.user = user;
        this.wbsApi = RetrofitHelper.provideWbsRetrofit().create(WbsApi.class);
        this.project = project;
    }

    @Override
    public String getWbs() throws Exception {
        ResponseModel<String> response = wbsApi.getWbs(project.getId()).execute().body();

        exceptionValidator.validate(response);
        return response.getData().trim();
    }

    @Override
    public String executeWbsCommand(WbsCommand command) throws Exception {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy dd MMM.")
                .create();
        String bodyJson = gson.toJson(command);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), bodyJson);
        ResponseModel<String> response = wbsApi.executeWbsCommand(project.getId(), user.getId(), body).execute().body();

        exceptionValidator.validate(response);
        return response.getData();
    }

    @Override
    public List<TodoTask> getTodolist(int userId) throws Exception {
        ResponseModel<List<TodoTask>> response = wbsApi.getTodolist(project.getId(), userId).execute().body();

        exceptionValidator.validate(response);
        return response.getData();
    }

    @Override
    public List<TodoTask> filterTasksByStatus(TodoTask.Status status) throws Exception {
        return null;
    }

    @Override
    public List<ReviewTaskCard> getReviewTaskCards() throws Exception {
        ResponseModel<List<ReviewTaskCard>> response = wbsApi.getReviewTaskCards(project.getId()).execute().body();

        exceptionValidator.validate(response);
        return response.getData();
    }

    private interface WbsApi{
        String RESOURCE = "projects/{projectId}/wbs";

        @GET(RESOURCE)
        public Call<ResponseModel<String>> getWbs(@Path("projectId") int projectId);

        @GET(RESOURCE + "/todolist/{userId}")
        public Call<ResponseModel<List<TodoTask>>> getTodolist(@Path("projectId") int projectId,
                                                               @Path("userId") int userId);

        @GET(RESOURCE + "/reviewCards")
        public Call<ResponseModel<List<ReviewTaskCard>>> getReviewTaskCards(@Path("projectId") int projectId);

        @Headers("Content-Type: application/json")
        @POST(RESOURCE)
        public Call<ResponseModel<String>> executeWbsCommand(@Path("projectId") int projectId,
                                                             @Query("userId") int userId,
                                                             @Body RequestBody command);
    }
}
