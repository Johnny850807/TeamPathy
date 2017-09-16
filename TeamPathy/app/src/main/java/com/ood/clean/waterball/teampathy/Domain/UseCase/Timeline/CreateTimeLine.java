package com.ood.clean.waterball.teampathy.Domain.UseCase.Timeline;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Timeline;
import com.ood.clean.waterball.teampathy.Domain.Repository.TimeLineRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;


@ProjectScope
public class CreateTimeLine extends UseCase<Timeline,Timeline> {
    private TimeLineRepository timeLineRepository;

    @Inject
    public CreateTimeLine(ThreadingObservableFactory threadingObservableFactory, TimeLineRepository timeLineRepository) {
        super(threadingObservableFactory);
        this.timeLineRepository = timeLineRepository;
    }

    @Override
    protected Observable<Timeline> buildUseCaseObservable(final Timeline timeline) {
        return Observable.create(new ObservableOnSubscribe<Timeline>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Timeline> e) throws Exception {
                e.onNext(timeLineRepository.create(timeline));
                e.onComplete();
            }
        });
    }
}
