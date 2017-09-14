package com.ood.clean.waterball.teampathy.Presentation.Presenter;


import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Timeline.CreateTimeLine;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Timeline.DeleteTimeLine;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Timeline.GetTimeLineList;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.Timeline;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.CrudPresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

@ProjectScope
public class TimelinesPresenterImp implements CrudPresenter<Timeline>{
    CrudView<Timeline> timelinesView;
    @Inject Project project;
    @Inject GetTimeLineList getTimeLineList;
    @Inject CreateTimeLine createTimeLine;
    @Inject DeleteTimeLine deleteTimeLine;

    @Inject
    public TimelinesPresenterImp() {}

    public void setTimelinesView(CrudView<Timeline> timelinesView){
        this.timelinesView = timelinesView;
    }

    @Override
    public void loadEntities(int page) {
        getTimeLineList.execute(new DefaultObserver<Timeline>() {
            @Override
            public void onNext(@NonNull Timeline timeline) {
                timelinesView.loadEntity(timeline);
            }

            @Override
            public void onComplete() {
                timelinesView.onLoadFinishNotify();
            }

            @Override
            public void onError(Throwable exception) {
                exception.printStackTrace();
                timelinesView.onOperationTimeout(exception);
            }
        } , page);
    }

    @Override
    public void create(final Timeline timeline) {
        createTimeLine.execute(new DefaultObserver<Timeline>() {
            @Override
            public void onNext(@NonNull Timeline timeline) {
                timelinesView.loadEntity(timeline);
                timelinesView.onCreateFinishNotify(timeline);
            }
        },timeline);
    }

    @Override
    public void update(Timeline timeline) {
        //todo
    }

    @Override
    public void delete(Timeline timeline) {
        deleteTimeLine.execute(new DefaultObserver<Timeline>() {
            @Override
            public void onNext(@NonNull Timeline timeline) {
                timelinesView.onDeleteFinishNotify(timeline);
            }
        }, timeline);
    }

    @Override
    public void onResume() {}

    @Override
    public void onDestroy() {
        createTimeLine.dispose();
        getTimeLineList.dispose();
        deleteTimeLine.dispose();
    }
}
