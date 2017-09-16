package com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository;


import com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit.RetrofitHelper;
import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory.ExceptionConverter;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberDetails;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Position;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Repository.OfficeRepository;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

@ProjectScope
public class OfficeRetrofitRepository implements OfficeRepository{
    private Project project;
    private ExceptionConverter exceptionConverter;
    private OfficeApi officeApi;

    @Inject
    public OfficeRetrofitRepository(ExceptionConverter exceptionConverter,
                                    Retrofit retrofit,
                                    Project project) {

        this.exceptionConverter = exceptionConverter;
        this.officeApi = RetrofitHelper.provideWbsRetrofit().create(OfficeApi.class);
        this.project = project;
    }

    @Override
    public List<MemberIdCard> getMemberIdCardList() throws Exception {
        ResponseModel<List<MemberIdCard>> response = officeApi.getMemberCards(project.getId()).execute().body();
        // API 回傳的 task 沒有  parent
        exceptionConverter.validate(response);
        return response.getData();
    }

    @Override
    public MemberDetails getMemberDetails(int userId) throws Exception {
        return null;
    }

    @Override
    public MemberDetails changeMemberPosition(int userId, Position position) throws Exception {
        return null;
    }

    @Override
    public void evictMember(int userId) throws Exception {

    }

    @Override
    public void notifyAllMembers(String content) throws Exception {

    }


    interface OfficeApi{
        String RESOURCE = "projects/{projectId}/office";

        @GET(RESOURCE + "/cards")
        public Call<ResponseModel<List<MemberIdCard>>> getMemberCards(@Path("projectId") int projectId);

    }
}
