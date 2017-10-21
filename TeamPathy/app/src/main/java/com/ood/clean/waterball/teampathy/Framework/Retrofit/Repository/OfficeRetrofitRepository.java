package com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository;


import com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit.RetrofitHelper;
import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.validator.ExceptionValidator;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Position;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.OfficeRepository;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

@ProjectScope
public class OfficeRetrofitRepository implements OfficeRepository{
    private User user;
    private Project project;
    private ExceptionValidator exceptionValidator;
    private OfficeApi officeApi;

    @Inject
    public OfficeRetrofitRepository(ExceptionValidator exceptionValidator,
                                    Retrofit retrofit,
                                    User user,
                                    Project project) {
        this.exceptionValidator = exceptionValidator;
        this.user = user;
        this.officeApi = RetrofitHelper.provideWbsRetrofit().create(OfficeApi.class);
        this.project = project;
    }

    @Override
    public List<MemberIdCard> getMemberIdCardList() throws Exception {
        ResponseModel<List<MemberIdCard>> response = officeApi.getMemberCards(project.getId()).execute().body();
        // API 回傳的 task 沒有  parent
        exceptionValidator.validate(response);
        return response.getData();
    }

    @Override
    public Member changeMemberPosition(int memberId, Position position) throws Exception {
        ResponseModel<Member> response = officeApi.changeMemberPosition(project.getId(),
                user.getId(), memberId, position.toString()).execute().body();

        exceptionValidator.validate(response);
        return response.getData();
    }


    @Override
    public void bootMember(int memberId) throws Exception {
        ResponseModel<Void> response = officeApi.bootMember(project.getId(),
                user.getId(), memberId).execute().body();

        exceptionValidator.validate(response);
    }


    @Override
    public Member leaderHandover(int memberId) throws Exception {
        ResponseModel<Member> response = officeApi.leaderHandover(project.getId(),
                user.getId(), memberId).execute().body();

        exceptionValidator.validate(response);
        return response.getData();
    }


    @Override
    public void notifyAllMembers(String content) throws Exception {

    }


    interface OfficeApi{
        String RESOURCE = "projects/{projectId}/office";

        @GET(RESOURCE + "/cards")
        public Call<ResponseModel<List<MemberIdCard>>> getMemberCards(@Path("projectId") int projectId);

        @PUT(RESOURCE + "/changePosition")
        public Call<ResponseModel<Member>> changeMemberPosition(@Path("projectId") int projectId,
                                                                @Query("leaderId") int leaderId,
                                                                @Query("memberId") int memberId,
                                                                @Query("position") String position);

        @PUT(RESOURCE + "/boot")
        public Call<ResponseModel<Void>> bootMember(@Path("projectId") int projectId,
                                                                  @Query("leaderId") int leaderId,
                                                                  @Query("memberId") int memberId);

        @PUT(RESOURCE + "/handover")
        public Call<ResponseModel<Member>> leaderHandover(@Path("projectId") int projectId,
                                                                      @Query("leaderId") int leaderId,
                                                                      @Query("memberId") int memberId);

    }
}
