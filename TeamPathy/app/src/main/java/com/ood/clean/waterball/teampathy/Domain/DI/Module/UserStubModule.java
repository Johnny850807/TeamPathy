package com.ood.clean.waterball.teampathy.Domain.DI.Module;


import android.content.Context;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.UserScope;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.ProjectRepository;
import com.ood.clean.waterball.teampathy.Stub.ProjectRepositoryStub;

import dagger.Module;
import dagger.Provides;

@Module
public class UserStubModule {
    private User user;

    public UserStubModule(User user) {
        this.user = user;
    }

    @Provides @UserScope
    public User provideUser(){
        return user;
    }

    @Provides @UserScope
    public ProjectRepository provideProjectRepository(Context context){
        return new ProjectRepositoryStub(user, context);
    }

}
