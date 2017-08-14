package com.ood.clean.waterball.teampathy.Stub;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.ResourceNotFoundException;
import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.Domain.Model.IssueComment;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.IssueRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;



@ProjectScope
public class IssueRepositoryStub implements IssueRepository {
    private Project project;
    private User user;
    private static Map<Project, Set<Issue>> issueDBStub = new HashMap<>();

    @Inject
    public IssueRepositoryStub(Project project, User user) {
        this.project = project;
        this.user = user;
        if (!issueDBStub.containsKey(project))
            issueDBStub.put(project,createIssueStubs());
    }

    @Override
    public Issue create(Issue entity) throws Exception {
        Thread.sleep(2000);
        issueDBStub.get(project).add(entity);
        return entity;
    }

    @Override
    public Issue update(Issue entity) throws Exception {
        Thread.sleep(2000);
        issueDBStub.get(project).add(entity);
        return entity;
    }

    @Override
    public Issue delete(Issue entity) throws Exception {
        Thread.sleep(2000);
        issueDBStub.get(project).remove(entity);
        return entity;
    }


    @Override
    public List<Issue> readList(final int page) throws Exception {
        Thread.sleep(2000);
        return new ArrayList<>(issueDBStub.get(project));
    }

    @Override
    public Issue readDetails(int id) throws ResourceNotFoundException, InterruptedException {
        Thread.sleep(2000);
        for ( Issue issue : issueDBStub.get(project) )
            if (issue.getId() == id)
                return issue;
        throw new ResourceNotFoundException();
    }

    @Override
    public void deleteIssueCategory(String category) throws Exception {
        Thread.sleep(2000);
        project.getIssueCategoryList().remove(category);
    }

    @Override
    public void addIssueCategory(String issueCategory) throws Exception {
        Thread.sleep(2000);
        project.getIssueCategoryList().add(issueCategory);
    }

    private Set<Issue> createIssueStubs(){
        Set<Issue> issues = new HashSet<>();
        issues.add(new Issue(user,"WooTalk很爛", "不想用了...","議題"));
        issues.add(new Issue(user,"班會有學分嗎?","如題，班會有學分??","討論"));
        issues.add(new Issue(user,"TeamPathy","我們行不行 ? 絕對沒問題 !!! ","議題"));
        issues.add(new Issue(user,"TeamPathy","我們行不行 ? 絕對沒問題 !!! ","議題"));
        issues.add(new Issue(user,"TeamPathy","我們行不行 ? 絕對沒問題 !!! ","議題"));
        issues.add(new Issue(user,"TeamPathy","我們行不行 ? 絕對沒問題 !!! ","議題"));
        List<IssueComment> comments = createCommentStubs();
        for (Issue issue : issues)
            issue.setComments(comments);
        return issues;
    }

    private List<IssueComment> createCommentStubs(){
        List<IssueComment> comments = new ArrayList<>();
        comments.add(new IssueComment(new User("曾韋傑",""),"I will put the"));
        comments.add(new IssueComment(new User("黃嘉偉",""),"謝謝!!"));
        comments.add(new IssueComment(new User("Wang Ning",""),"who that be"));
        comments.add(new IssueComment(new User("黃嘉偉",""),"我覺得不行!!"));
        comments.add(new IssueComment(new User("黃嘉偉",""),"我覺得很普通!!"));
        comments.add(new IssueComment(new User("Wang Ning",""),"請問問題出在哪裡?"));
        comments.add(new IssueComment(new User("曾韋傑",""),"節拍阿幹"));
        comments.add(new IssueComment(new User("林宗億",""),"因為你用英文名字"));
        return comments;
    }
}
