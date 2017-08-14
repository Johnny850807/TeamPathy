package com.ood.clean.waterball.teampathy.Domain.Repository;

import com.ood.clean.waterball.teampathy.Domain.Model.Issue;

public interface IssueRepository extends Repository<Issue> {

    void deleteIssueCategory(String category) throws Exception;

    void addIssueCategory(String issueCategory) throws Exception;
}
