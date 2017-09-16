package com.ood.clean.waterball.teampathy.Domain.UseCase.Project;

import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Repository.ProjectRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.UseCase;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

public class JoinProject extends UseCase<Project, JoinProject.Params> {

    private ProjectRepository projectRepository;

    public static JoinProject.Params noPassword(Project project){
        return new Params(project,Project.NO_PASSWORD);
    }

    @Inject
    public JoinProject(ThreadingObservableFactory threadingObservableFactory, ProjectRepository projectRepository) {
        super(threadingObservableFactory);
        this.projectRepository = projectRepository;
    }

    @Override
    protected Observable<Project> buildUseCaseObservable(final JoinProject.Params params) {
        return Observable.create(new ObservableOnSubscribe<Project>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Project> e) throws Exception {
                projectRepository.joinProject(params);
                e.onNext(params.getProject());
                e.onComplete();
            }
        });
    }


    public static class Params {
        private Project project;
        private String password;

        public Params(Project project, String password) {
            this.project = project;
            this.password = password;
        }

        public Project getProject() {
            return project;
        }

        public void setProject(Project project) {
            this.project = project;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
