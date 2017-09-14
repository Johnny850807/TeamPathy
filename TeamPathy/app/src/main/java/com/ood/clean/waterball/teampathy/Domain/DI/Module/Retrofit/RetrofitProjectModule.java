package com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory.ExceptionConverter;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.IssueRepository;
import com.ood.clean.waterball.teampathy.Domain.Repository.OfficeRepository;
import com.ood.clean.waterball.teampathy.Domain.Repository.TimeLineRepository;
import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.IssueRetrofitRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.OfficeRetrofitRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.TimelineRetrofitRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.WbsRetrofitRepository;

import javax.inject.Named;

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
    public IssueRepository provideIssueRepository(User user, @Named("DateFormatRetrofit")Retrofit retrofit, ExceptionConverter exceptionConverter){
        return new IssueRetrofitRepository(project, retrofit, exceptionConverter, user);
    }

    @Provides @ProjectScope
    public TimeLineRepository provideTimelineRepository(ExceptionConverter exceptionConverter,
                                                        User user,
                                                        Project project,
                                                        @Named("DateFormatRetrofit")Retrofit retrofit){
        return new TimelineRetrofitRepository(exceptionConverter, project, user, retrofit);
    }

    @Provides @ProjectScope
    public OfficeRepository provideOfficeRepository(ExceptionConverter exceptionConverter,
                                                    @Named("WbsRetrofit") Retrofit retrofit,
                                                    Project project){
        return new OfficeRetrofitRepository(exceptionConverter, retrofit, project);
    }

    @Provides @ProjectScope
    public WbsRepository provideWbsRepository(ExceptionConverter exceptionConverter,
                                              @Named("DateFormatRetrofit")Retrofit retrofit,
                                              Project project){
        return new WbsRetrofitRepository(exceptionConverter, retrofit, project);
    }

}
