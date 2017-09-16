package com.ood.clean.waterball.teampathy.Domain.UseCase.Timeline;


import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Timeline;
import com.ood.clean.waterball.teampathy.Domain.Repository.TimeLineRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

@ProjectScope
public class GetTimeLineList extends UseCase<Timeline, Integer> {

    TimeLineRepository timeLineRepository;

    @Inject
    public GetTimeLineList(ThreadingObservableFactory threadingObservableFactory, TimeLineRepository timeLineRepository) {
        super(threadingObservableFactory);
        this.timeLineRepository = timeLineRepository;
    }

    @Override
    protected Observable<Timeline> buildUseCaseObservable(final Integer page) {
        return Observable.create(new ObservableOnSubscribe<Timeline>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Timeline> e) throws Exception {
                List<Timeline> timelineList = timeLineRepository.readList(page);
                for ( Timeline timeline : timelineList )
                    e.onNext(timeline);
                e.onComplete();
            }
        });
    }
}
