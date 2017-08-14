package com.ood.clean.waterball.teampathy.Stub;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.IssueScope;
import com.ood.clean.waterball.teampathy.Domain.Exception.ResourceNotFoundException;
import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.Domain.Model.IssueComment;
import com.ood.clean.waterball.teampathy.Domain.Repository.IssueCommentRepository;

import java.util.List;

import javax.inject.Inject;

@IssueScope
public class IssueCommentRepositoryStub implements IssueCommentRepository {
    private Issue issue;

    @Inject
    public IssueCommentRepositoryStub(Issue issue) {
        this.issue = issue;
    }

    @Override
    public IssueComment create(IssueComment entity) throws Exception {
        Thread.sleep(2000);
        issue.getComments().add(entity);
        return entity;
    }

    @Override
    public IssueComment update(IssueComment entity) throws Exception {
        Thread.sleep(2000);
        if (!issue.getComments().contains(entity))
            throw new ResourceNotFoundException();
        issue.getComments().add(issue.getComments().indexOf(entity),entity);
        return entity;
    }

    @Override
    public IssueComment delete(IssueComment entity) throws Exception {
        Thread.sleep(2000);
        issue.getComments().remove(entity);
        return entity;
    }

    @Override
    public List<IssueComment> readList(int page) throws Exception {
        Thread.sleep(2000);
        return issue.getComments();
    }

    @Override
    public IssueComment readDetails(int id) throws Exception {
        Thread.sleep(2000);
        for ( IssueComment comment : issue.getComments() )
            if (comment.getId() == id)
                return comment;
        throw new ResourceNotFoundException();
    }

}
