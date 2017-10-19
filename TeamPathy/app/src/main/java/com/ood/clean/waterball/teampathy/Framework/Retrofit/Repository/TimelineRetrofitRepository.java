package com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.validator.ExceptionValidator;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.Timeline;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.TimeLineRepository;

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
public class TimelineRetrofitRepository implements TimeLineRepository {
    private ExceptionValidator exceptionValidator;
    private Project project;
    private User user;
    private TimelineApi timelineApi;

    @Inject
    public TimelineRetrofitRepository(ExceptionValidator exceptionValidator,
                                      Project project,
                                      User user,
                                      Retrofit retrofit) {
        this.exceptionValidator = exceptionValidator;
        this.user = user;
        this.project = project;

        timelineApi = retrofit.create(TimelineApi.class);
    }

    @Override
    public Timeline create(Timeline entity) throws Exception {
        ResponseModel<Timeline> response = timelineApi.create(project.getId(), user.getId(), entity.toFieldMap())
                .execute().body();

        exceptionValidator.validate(response);
        return response.getData();
    }

    @Override
    public Timeline update(Timeline entity) throws Exception {
        return null;  //todo
    }

    @Override
    public Timeline delete(Timeline entity) throws Exception {
        ResponseModel<Void> response = timelineApi.delete(project.getId(), entity.getId())
                .execute().body();

        exceptionValidator.validate(response);
        return entity;
    }

    @Override
    public List<Timeline> readList(int page) throws Exception {
        ResponseModel<List<Timeline>> response = timelineApi.getList(project.getId(), page)
                .execute().body();

        exceptionValidator.validate(response);
        return response.getData();
    }

    @Override
    public Timeline readDetails(int id) throws Exception {
        return null;
    }

    interface TimelineApi{
        String RESOURCE = "projects/{projectId}/timelines";

        @FormUrlEncoded
        @POST(RESOURCE)
        public Call<ResponseModel<Timeline>> create(@Path("projectId") int projectId,
                                    @Query("userId") int userId,
                                    @FieldMap Map<String,String> fields);

        @GET(RESOURCE)
        public Call<ResponseModel<List<Timeline>>> getList(@Path("projectId") int projectId,
                                                           @Query("page") int page);

        @DELETE(RESOURCE + "/{timelineId}")
        public Call<ResponseModel<Void>> delete(@Path("projectId") int projectId,
                                                           @Path("timelineId") int timelineId);
    }
}
