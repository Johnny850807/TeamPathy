package com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit;

import android.content.Context;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory.ExceptionConverter;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskXmlTranslator;
import com.ood.clean.waterball.teampathy.Domain.Repository.IssueRepository;
import com.ood.clean.waterball.teampathy.Domain.Repository.OfficeRepository;
import com.ood.clean.waterball.teampathy.Domain.Repository.TimeLineRepository;
import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.IssueRetrofitRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.TimelineRetrofitRepository;
import com.ood.clean.waterball.teampathy.Stub.OfficeRepositoryStub;
import com.ood.clean.waterball.teampathy.Stub.WbsRepositoryStub;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class RetrofitProjectModule {
    private Project project;
    private Member member;  //means the member details of the user in this project

    public RetrofitProjectModule(Project project, Member member) {
        this.project = project;
        this.member = member;
    }

    @Provides
    @ProjectScope
    public Project provideProject(){
        return project;
    }

    @Provides @ProjectScope
    public Member provideMember(){
        return member;
    }

    @Provides @ProjectScope
    public IssueRepository provideIssueRepository(User user, Retrofit retrofit, ExceptionConverter exceptionConverter){
        return new IssueRetrofitRepository(project, retrofit, exceptionConverter, user);
    }

    @Provides @ProjectScope
    public TimeLineRepository provideTimelineRepository(ExceptionConverter exceptionConverter,
                                                        User user,
                                                        Project project,
                                                        Retrofit retrofit){
        return new TimelineRetrofitRepository(exceptionConverter, project, user, retrofit);
    }

    @Provides @ProjectScope
    public OfficeRepository provideOfficeRepository(Project project, Context context){
        return new OfficeRepositoryStub(project, context);
    }

    @Provides @ProjectScope
    public WbsRepository provideWbsRepository(Project project, TaskXmlTranslator taskXmlTranslator, Context context){
        return new WbsRepositoryStub(project, taskXmlTranslator,context);
    }

}
