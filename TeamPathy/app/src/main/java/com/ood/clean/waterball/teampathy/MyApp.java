package com.ood.clean.waterball.teampathy;

import android.app.Activity;
import android.app.Application;

import com.ood.clean.waterball.teampathy.Domain.DI.Component.ActivityComponent;
import com.ood.clean.waterball.teampathy.Domain.DI.Component.ApplicationComponent;
import com.ood.clean.waterball.teampathy.Domain.DI.Component.DaggerApplicationComponent;
import com.ood.clean.waterball.teampathy.Domain.DI.Component.IssueComponent;
import com.ood.clean.waterball.teampathy.Domain.DI.Component.ProjectComponent;
import com.ood.clean.waterball.teampathy.Domain.DI.Component.UserComponent;
import com.ood.clean.waterball.teampathy.Domain.DI.Component.WbsComponent;
import com.ood.clean.waterball.teampathy.Domain.DI.Module.ActivityModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Module.ApplicationModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit.RetrofitIssueModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit.RetrofitProjectModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Module.Retrofit.RetrofitUserModule;
import com.ood.clean.waterball.teampathy.Domain.DI.Module.WbsModule;
import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;


public class MyApp extends Application {
    private ApplicationComponent applicationComponent;
    private ActivityComponent activityComponent;
    private UserComponent userComponent;
    private ProjectComponent projectComponent;
    private IssueComponent issueComponent;
    private WbsComponent wbsComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule())
                .build();
    }

    //ActivityComponent: Activity, context ...
    public static ActivityComponent createActivityComponent(Activity activity) {
        MyApp app = (MyApp) activity.getApplication();
        app.activityComponent = app.applicationComponent.plus
                (new ActivityModule(activity));
        return app.activityComponent;
    }

    public static UserComponent createUserComponent(Activity activity,User user) {
        MyApp app = (MyApp) activity.getApplication();
        app.userComponent = app.activityComponent.plus
                (new RetrofitUserModule(user));
        return app.userComponent;
    }

    public static ProjectComponent createProjectComponent(Activity activity, Project project, Member member) {
        MyApp app = (MyApp) activity.getApplication();
        app.projectComponent = app.userComponent.plus
                (new RetrofitProjectModule(project, member));
        return app.projectComponent;
    }

    public static IssueComponent createIssueComponent(Activity activity, Issue issue) {
        MyApp app = (MyApp) activity.getApplication();
        app.issueComponent = app.projectComponent.plus
                (new RetrofitIssueModule(issue));
        return app.issueComponent;
    }

    public static WbsComponent createWbsComponent(Activity activity) {
        MyApp app = (MyApp) activity.getApplication();
        app.wbsComponent = app.projectComponent.plus(new WbsModule());
        return app.wbsComponent;
    }

    public static ApplicationComponent getApplicationComponent(Activity activity){
        return ((MyApp)activity.getApplication()).applicationComponent;
    }

    public static UserComponent getUserComponent(Activity activity) {
        return ((MyApp)activity.getApplication()).userComponent;
    }

    public static ProjectComponent getProjectComponent(Activity activity) {
        return ((MyApp)activity.getApplication()).projectComponent;
    }

    public static IssueComponent getIssueComponent(Activity activity) {
        return ((MyApp)activity.getApplication()).issueComponent;
    }

    public static WbsComponent getWbsComponent(Activity activity){
        return ((MyApp)activity.getApplication()).wbsComponent;
    }
}
