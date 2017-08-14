package com.ood.clean.waterball.teampathy.Stub;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.ResourceNotFoundException;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.Timeline;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.TimeLineRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

@ProjectScope
public class TimeLineRepositoryStub implements TimeLineRepository {

    private Project project;
    private static Map<Project, Set<Timeline>> timelineDBStub = new HashMap<>();

    @Inject
    public TimeLineRepositoryStub(Project project) {
        this.project = project;
        putProjectInMapIfNotContains();
    }

    private void putProjectInMapIfNotContains(){
        if (!timelineDBStub.containsKey(project))
            timelineDBStub.put(project, createTimelineStubs());
    }


    @Override
    public Timeline create(final Timeline entity) throws Exception {
        Thread.sleep(2000);  //stub
        timelineDBStub.get(project).add(entity);
        return entity;
    }

    @Override
    public Timeline update(final Timeline entity) throws Exception {
        timelineDBStub.get(project).add(entity);
        return entity;
    }

    @Override
    public Timeline delete(final Timeline entity) throws Exception {
        timelineDBStub.get(project).remove(entity);
        return entity;
    }

    @Override
    public List<Timeline> readList(final int page) throws Exception {
        Thread.sleep(2000);  //stub
        return new ArrayList<>(timelineDBStub.get(project));
    }

    @Override
    public Timeline readDetails(final int id) throws ResourceNotFoundException {
        for ( Timeline timeline : timelineDBStub.get(project) )
            if (timeline.getId() == id)
                return timeline;
        throw new ResourceNotFoundException();
    }

    private Set<Timeline> createTimelineStubs() {
        Set<Timeline> timelines = new HashSet<>();
        timelines.add(new Timeline(new User("曾韋傑","http://pic.pimg.tw/wowokitchen/1422587200-2903474872.png?v=1422587201"),"I will put the"));
        timelines.add(new Timeline(new User("黃嘉偉","https://s-media-cache-ak0.pinimg.com/236x/55/46/5d/55465d97979542886df9757e220e043c--smile-teeth-dental-care.jpg"),"謝謝!!"));
        timelines.add(new Timeline(new User("Wang Ning","https://yt3.ggpht.com/-a6GAEH0zegM/AAAAAAAAAAI/AAAAAAAAAAA/Xk8tMdynYaI/s900-c-k-no-mo-rj-c0xffffff/photo.jpg"),"who that be"));
        timelines.add(new Timeline(new User("黃嘉偉","https://s-media-cache-ak0.pinimg.com/236x/55/46/5d/55465d97979542886df9757e220e043c--smile-teeth-dental-care.jpg"),"我覺得不行!!"));
        timelines.add(new Timeline(new User("黃嘉偉","https://s-media-cache-ak0.pinimg.com/236x/55/46/5d/55465d97979542886df9757e220e043c--smile-teeth-dental-care.jpg"),"我覺得很普通!!"));
        timelines.add(new Timeline(new User("Wang Ning","https://yt3.ggpht.com/-a6GAEH0zegM/AAAAAAAAAAI/AAAAAAAAAAA/Xk8tMdynYaI/s900-c-k-no-mo-rj-c0xffffff/photo.jpg"),"請問問題出在哪裡?"));
        timelines.add(new Timeline(new User("曾韋傑","http://pic.pimg.tw/wowokitchen/1422587200-2903474872.png?v=1422587201"),"節拍阿幹"));
        timelines.add(new Timeline(new User("林宗億","http://www.cndog.net/upimg/2011/9/2011929163826619.jpg"),"英文名字"));
        return timelines;
    }

}
