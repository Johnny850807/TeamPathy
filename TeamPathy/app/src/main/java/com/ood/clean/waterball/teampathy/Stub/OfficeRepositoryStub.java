package com.ood.clean.waterball.teampathy.Stub;

import android.content.Context;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberDetails;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Position;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.Domain.Repository.OfficeRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@ProjectScope
public class OfficeRepositoryStub implements OfficeRepository {
    private Project project;
    private Context context;
    @Inject
    public OfficeRepositoryStub(Project project, Context context) {
        this.project = project;
        this.context = context;
    }


    @Override
    public List<MemberIdCard> getMemberIdCardList() throws Exception {
        User zong = new User("林宗億","http://i.imgur.com/4wXEKrP.png");
        Member memberZong = new Member(zong,new MemberDetails(Position.manager,1700));
        User wb = new User("水球潘","http://i.imgur.com/NCCXqIj.jpg");
        Member memberWb = new Member(wb,new MemberDetails(Position.leader,4500));
        User zheng = new User("曾韋傑","http://i.imgur.com/c3qnbkg.png");
        Member memberZheng = new Member(zheng,new MemberDetails(Position.member,700));
        User chia = new User("花柳齋","http://i.imgur.com/DuhZSwy.png");
        Member memberchia = new Member(chia,new MemberDetails(Position.member,600));
        TodoTask todoTask1 = new TodoTask("ER圖設計","設計","資料庫設計",330, "",TodoTask.Status.assigned,0);
        TodoTask todoTask4 = new TodoTask("Android架構","實作","架構實作",1500, "", TodoTask.Status.assigned,0);
        TodoTask todoTask2 = new TodoTask("API 學習","實作","API文件閱讀實作",170, "", TodoTask.Status.assigned,1);
        TodoTask todoTask3 = new TodoTask("初審文案整理","文案","展出整理",400, "", TodoTask.Status.assigned,1);

        List<MemberIdCard> memberIdCardModelList = new ArrayList<>();
        memberIdCardModelList.add(new MemberIdCard(memberZong,todoTask2));
        memberIdCardModelList.add(new MemberIdCard(memberWb,todoTask4));
        memberIdCardModelList.add(new MemberIdCard(memberZheng,todoTask1));
        memberIdCardModelList.add(new MemberIdCard(memberchia,todoTask3));
        return memberIdCardModelList;
    }


    @Override
    public Member changeMemberPosition(int userId, Position position) throws Exception {
        throw new RuntimeException("Not supported.");
    }

    @Override
    public Member leaderHandover(int memberId) throws Exception {
        return null;
    }

    @Override
    public void bootMember(int userId) throws Exception {
        //todo
    }

    @Override
    public void notifyAllMembers(String content) throws Exception {
        //todo
    }
}
