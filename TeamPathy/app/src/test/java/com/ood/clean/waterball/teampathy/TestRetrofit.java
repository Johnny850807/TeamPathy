package com.ood.clean.waterball.teampathy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory.ExceptionConverter;
import com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory.ExceptionConverterImp;
import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.Domain.Model.IssueComment;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Position;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.Timeline;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.ServerConstant;
import com.ood.clean.waterball.teampathy.Domain.Repository.IssueCommentRepository;
import com.ood.clean.waterball.teampathy.Domain.Repository.IssueRepository;
import com.ood.clean.waterball.teampathy.Domain.Repository.TimeLineRepository;
import com.ood.clean.waterball.teampathy.Domain.Repository.UserRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.GetMemberInfo;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.JoinProject;
import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignIn;
import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignUp;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.IssueCommentRetrofitRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.IssueRetrofitRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.ProjectRetrofitRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.TimelineRetrofitRepository;
import com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository.UserRetrofitRespository;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;



public class TestRetrofit {
    private Retrofit retrofit;

    @Before
    public void init(){
        retrofit = provideRetrofit();
    }

    @Test
    public void test() throws Exception{
        ExceptionConverter exceptionConverter = new ExceptionConverterImp();
        /** test users **/
        UserRepository usersRepos = new UserRetrofitRespository(retrofit, exceptionConverter);
        String account = randomString();
        String password = randomString();
        User signUpuser = usersRepos.signUp(new SignUp.Params(account,password,randomString(),null));
        assertEquals(signUpuser.getImageUrl(), "https://cdn.pixabay.com/photo/2012/04/26/19/43/profile-42914_640.png");

        User signInUser = usersRepos.signIn(new SignIn.Params(account, password));
        assertEquals(signInUser.getId(), signUpuser.getId());

        /** test projects **/
        ProjectRetrofitRepository projectRepos =
                new ProjectRetrofitRepository(retrofit, exceptionConverter, signInUser);

        //test search
        List<Project> searchProject = projectRepos.searchProjectByName("吃");
        assertNotNull(searchProject);

        // test create
        Project newProject = new Project(randomString(), randomString(),
                randomString(), null);
        Project project = projectRepos.create(newProject);
        assertEquals(project.getName(), newProject.getName());

        //test read details
        Project firstProject = projectRepos.readDetails(1);

        //test join
        projectRepos.joinProject(new JoinProject.Params(firstProject, "password"));
        signInUser = usersRepos.signIn(new SignIn.Params(account, password));  //refresh sign in user
        assertTrue(signInUser.hasJoinedTo(firstProject));

        // test member info
        Member memberInfo = projectRepos.getMemberInfo(new GetMemberInfo.Params(signInUser.getId(),
                firstProject.getId()));
        assertEquals(Position.member, memberInfo.getMemberDetails().getPosition());
        Member memberInfoForLeader = projectRepos.getMemberInfo(new GetMemberInfo.Params(signInUser.getId(),
                project.getId()));
        assertEquals(Position.leader, memberInfoForLeader.getMemberDetails().getPosition());

        //test issue
        IssueRepository issueRepository =
                new IssueRetrofitRepository(firstProject,retrofit,exceptionConverter,signInUser);

        //test issue category
        issueRepository.addIssueCategory("投票");
        issueRepository.addIssueCategory("不能存在的分類");
        issueRepository.deleteIssueCategory("不能存在的分類");

        firstProject = projectRepos.readDetails(1);
        assertTrue(firstProject.getIssueCategoryList().contains("投票"));
        assertFalse(firstProject.getIssueCategoryList().contains("不能存在的分類"));

        //test get issue details
        Issue firstIssue = issueRepository.readDetails(1);
        assertEquals(1, firstIssue.getId());
        assertNotNull(firstIssue.getComments());

        IssueComment firstComment = firstIssue.getComments().get(0);
        assertNotNull(firstComment.getContent());
        assertNotNull(firstComment.getPoster());

        //test issue comment
        IssueCommentRepository issueCommentRepository = new IssueCommentRetrofitRepository(exceptionConverter, signInUser,
                firstProject, firstIssue, retrofit);
        String content = randomString();
        IssueComment issueComment = new IssueComment(signInUser, content);
        IssueComment newIssueComment = issueCommentRepository.create(issueComment);
        assertNotSame(0, newIssueComment.getId());
        assertEquals(content, newIssueComment.getContent());


        //test get comment list
        List<IssueComment> comments = issueCommentRepository.readList(0);
        assertNotSame(0, comments.size());

        //test get issue list
        List<Issue> issueListOfFirstProject = issueRepository.readList(0);
        assertNotSame(0, issueListOfFirstProject.size());
        Issue firstIssueOfFirstProject = issueListOfFirstProject.get(0);
        assertNotNull(firstIssueOfFirstProject.getName());
        assertNotNull(firstIssueOfFirstProject.getContent());
        assertNotNull(firstIssueOfFirstProject.getPoster());
        assertNotSame(0, firstIssueOfFirstProject.getComments().size());

        //test create issue
        String title = randomString();
        Issue issue = new Issue(signInUser, title, randomString(),
                firstProject.getIssueCategoryList().get(0));
        Issue newIssue = issueRepository.create(issue);

        assertNotSame(0, newIssue.getId());
        assertEquals(title, newIssue.getName());
        assertEquals(signInUser, newIssue.getPoster());

        //test timeline
        TimeLineRepository timeLineRepository = new TimelineRetrofitRepository(exceptionConverter,
                firstProject, signInUser, retrofit);

        //test create timeline
        String timelinecontent = randomString();
        Timeline timeline = new Timeline(signInUser, timelinecontent);
        Timeline newTimeline = timeLineRepository.create(timeline);
        assertEquals(timeline.getContent(), newTimeline.getContent());
        assertEquals(signInUser, newTimeline.getPoster());
        assertNotSame(0, newTimeline.getId());

        //test delete timeline
        timeLineRepository.delete(newTimeline);

        //test timeline list
        List<Timeline> timelines = timeLineRepository.readList(0);
        assertNotSame(0, timelines.size());

        // test if newTimeline has been deleted
        for (Timeline t : timelines)
            assertNotSame(newTimeline, t);

    }

    private String randomString(){
        StringBuilder strb = new StringBuilder();
        Random random = new Random();
        strb.append("Test");
        for ( int i = 0 ; i < 5 ; i ++ )
            strb.append((char)(97 + random.nextInt(26)));
        return strb.toString();
    }

    private Retrofit provideRetrofit(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();

        return new Retrofit.Builder()
                .baseUrl(ServerConstant.BASE_SERVER_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
