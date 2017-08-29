package com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory.ExceptionConverter;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;
import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

@ProjectScope
public class WbsRetrofitRepository implements WbsRepository {
    private Project project;
    private ExceptionConverter exceptionConverter;
    private WbsApi wbsApi;

    @Inject
    public WbsRetrofitRepository(ExceptionConverter exceptionConverter,
                                 Retrofit retrofit,
                                 Project project) {
        this.exceptionConverter = exceptionConverter;
        this.wbsApi = retrofit.create(WbsApi.class);
        this.project = project;
    }

    @Override
    public String getWbs() throws Exception {
        ResponseModel<String> response = wbsApi.getWbs(project.getId()).execute().body();

        if (!exceptionConverter.isSuccessful(response))
            throw exceptionConverter.convert(response);

        return response.getData();
    }

    @Override
    public <Data extends TaskItem> String executeWbsCommand(WbsCommand<Data> command) throws Exception {
        ResponseModel<String> response = wbsApi.executeWbsCommand(project.getId(), command).execute().body();

        if (!exceptionConverter.isSuccessful(response))
            throw exceptionConverter.convert(response);

        return response.getData();
    }

    private interface WbsApi{
        String RESOURCE = "projects/{projectId}/wbs";

        @GET(RESOURCE)
        public Call<ResponseModel<String>> getWbs(@Path("projectId") int projectId);

        @Headers("Content-Type: application/json")
        @POST(RESOURCE)
        public Call<ResponseModel<String>> executeWbsCommand(@Path("projectId") int projectId,
                                                             @Body WbsCommand command);
    }
}
