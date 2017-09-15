package com.ood.clean.waterball.teampathy.Presentation.Interfaces;


import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignUp;
import com.ood.clean.waterball.teampathy.Domain.Model.User;

public interface SignUpPresenter {

    public interface SignUpView{
        void onSignUpSuccessfully(User user);
        void onImageUploadSuccessfully(String imageUrl);
        void onImageUploadError(Throwable err);

        void onAccountDuplicated();
    }

    void signUp(SignUp.Params params);
    void uploadImage(String imagePath);
}
