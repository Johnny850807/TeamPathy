package com.ood.clean.waterball.teampathy;

import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Repository.ProjectRepository;
import com.ood.clean.waterball.teampathy.Domain.Repository.UserRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignIn;
import com.ood.clean.waterball.teampathy.Stub.UserRepositoryStub;

import static junit.framework.Assert.assertEquals;


public class RepositoriesTest {
    private ProjectRepository repo;
    private UserRepository userRepository;
    private RepositoryTestHelper<Project> testHelper;

    //@Before
    public void init(){
        Project project = new Project();
        userRepository = new UserRepositoryStub();

        try {
            User user = userRepository.signIn(new SignIn.Params("",""));
            //repo = new ProjectRepositoryStub(user, context);
            testHelper = new RepositoryTestHelper<>(repo, new Project("Test","","","")
                    , new Project("Update","","",""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testRepositories() throws Exception {
        int currentSize = testHelper.getCurrentSize();
        testHelper.create100Entity();
        int resultSize = testHelper.getCurrentSize();
        assertEquals(currentSize+100,resultSize);

        testHelper.deleteFinal20Entity();
        resultSize -= 20;
        assertEquals(resultSize,testHelper.getCurrentSize());

        testHelper.updateID400Entity();
        assertEquals("Update",testHelper.get400sEntity().getName());
    }
    
}