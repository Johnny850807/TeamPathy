package com.ood.clean.waterball.teampathy.Stub;

import com.ood.clean.waterball.teampathy.Domain.Exception.AccountDuplicatedException;
import com.ood.clean.waterball.teampathy.Domain.Exception.UserNotFoundException;
import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignIn;
import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignUp;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.ood.clean.waterball.teampathy.Stub.ProjectRepositoryStub.IMAGE_URL_PROJECT;

@Singleton
public class UserRepositoryStub implements UserRepository {
    //user map 存 => 每個帳號 對應的 User
    private Map<SignIn.Params,User> userMap = new HashMap<>();

    @Inject
    public UserRepositoryStub() {
        SignIn.Params params = new SignIn.Params("","");
        User userStub = new User();
        userStub.setName("林宗億");
        userStub.setImageUrl("http://i.imgur.com/4wXEKrP.png");
        userStub.setProjectList(createProjectStubs());
        userMap.put(params,userStub);
    }

    @Override
    public User signIn(final SignIn.Params params) throws UserNotFoundException {
        if (!userMap.containsKey(params))
            throw new UserNotFoundException("Account error.");
        return userMap.get(params);
    }

    @Override
    public User signUp(final SignUp.Params params) throws AccountDuplicatedException {
        for ( SignIn.Params info : userMap.keySet() )
            if (info.getAccount().equals(params.getAccount()))
                throw new AccountDuplicatedException();
        SignIn.Params newParam = new SignIn.Params(params.getAccount(),params.getPassword());
        User newUser = new User();
        newUser.setName(params.getName());
        newUser.setImageUrl(params.getImageUrl());
        userMap.put(newParam, newUser);
        return newUser;
    }

    @Override
    public void signOut(final User user) {
        userMap.values().remove(user);
    }

    private List<Project> createProjectStubs(){
        List<Project> projectList = new ArrayList<>();
        projectList.add(new Project("真實TeamPathy", "軟體專案", "行動化團隊合作系統", IMAGE_URL_PROJECT));
        projectList.add(new Project("Chevrolet", "軟體專案", "行動化團隊合作系統", "http://www.carlogos.org/logo/Chevrolet-logo-2013-640x281.jpg"));
        projectList.add(new Project("Bentley", "軟體專案", "行動化團隊合作系統", "https://8096-presscdn-0-43-pagely.netdna-ssl.com/wp-content/uploads/2014/10/Bentley-Logo.jpg"));
        projectList.add(new Project("corvette", "軟體專案", "行動化團隊合作系統", "http://www.car-logos.org/wp-content/uploads/2011/09/corvette.png"));
        projectList.add(new Project("Maserati", "軟體專案", "行動化團隊合作系統", "http://www.car-brand-names.com/wp-content/uploads/2015/05/Maserati-logo.png"));
        projectList.add(new Project("SAAB", "軟體專案", "行動化團隊合作系統", "https://s-media-cache-ak0.pinimg.com/originals/c8/f9/70/c8f970e598b5340497fb6a7145a8f956.jpg"));
        projectList.add(new Project("JAGUAR", "軟體專案", "行動化團隊合作系統", "http://www.autocarbrands.com/wp-content/uploads/2014/04/jaguarlogo.jpg"));
        projectList.add(new Project("Anonymous", "軟體專案", "行動化團隊合作系統", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a6/Anonymous_emblem.svg/1200px-Anonymous_emblem.svg.png"));
        projectList.add(new Project("機器人之家", "機器學習", "行動化團隊合作系統", "https://image.flaticon.com/teams/slug/freepik.jpg"));
        projectList.add(new Project("Alfa Romeo", "軟體專案", "行動化團隊合作系統", "https://www.alfaromeousa.com/content/alfausa/en/the-badge/jcr:content/main/badge_head/main-logo.img.png/1498038795304.png"));
        projectList.add(new Project("Pepsi", "軟體專案", "行動化團隊合作系統", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/Pepsi_logo_2014.svg/2000px-Pepsi_logo_2014.svg.png"));
        projectList.add(new Project("Fox", "軟體專案", "行動化團隊合作系統", "https://upload.wikimedia.org/wikipedia/zh/thumb/a/a7/Firefox_Logo_1.0.svg/1072px-Firefox_Logo_1.0.svg.png"));
        projectList.add(new Project("我有FreeStyle", "軟體專案", "行動化團隊合作系統", "http://mediad.publicbroadcasting.net/p/wjct/files/201311/freestyle_03.gif"));
        projectList.add(new Project("台灣有嘻哈", "軟體專案", "行動化團隊合作系統", "http://img.mp.itc.cn/upload/20170724/86c34a02ee264845a53cee9abaabfd88_th.jpg"));
        projectList.add(new Project("寵物你我牠", "軟體專案", "行動化團隊合作系統", "http://logo.chuangyimao.com/uploads/logo/20150105/150105020517a299d90e8e.png"));

        for (Project project : projectList)
            project.setIssueCategoryList(createIssueCategoryList());
        return projectList;
    }

    private List<String> createIssueCategoryList(){
        List<String> issueCategoryList = new ArrayList<>();
        issueCategoryList.add("投票");
        issueCategoryList.add("Bug");
        issueCategoryList.add("討論");
        issueCategoryList.add("分享");
        issueCategoryList.add("心得");
        issueCategoryList.add("關鍵!");
        issueCategoryList.add("貢獻");
        issueCategoryList.add("教學");
        issueCategoryList.add("提醒");
        issueCategoryList.add("通知");
        issueCategoryList.add("懲罰");
        issueCategoryList.add("審核");
        return issueCategoryList;
    }

}
