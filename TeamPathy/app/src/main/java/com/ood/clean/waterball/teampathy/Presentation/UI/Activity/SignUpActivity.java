package com.ood.clean.waterball.teampathy.Presentation.UI.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ActivityScope;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignUp;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.CameraCropperUtils;
import com.ood.clean.waterball.teampathy.MyUtils.GlideHelper;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.SignUpPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.SignUpPresenterImp;
import com.ood.clean.waterball.teampathy.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ood.clean.waterball.teampathy.MyUtils.CameraCropperUtils.PICK_FROM_CAMERA;
import static com.ood.clean.waterball.teampathy.MyUtils.CameraCropperUtils.PICK_FROM_GALLERY;

@ActivityScope
public class SignUpActivity extends AppCompatActivity implements SignUpPresenter.SignUpView{
    @Inject SignUpPresenterImp signUpPresenter;
    @BindView(R.id.photoImg) ImageButton photoImg;
    @BindView(R.id.nameEd)EditText nameEd;
    @BindView(R.id.accountEd)EditText accountEd;
    @BindView(R.id.passwordEd)EditText passwordEd;
    @BindView(R.id.passwordConfirmEd)EditText passwordConfirmEd;
    @BindView(R.id.photoUploadingProgress)ProgressBar photoUploadingProgresses;
    ProgressDialog progressDialog;
    String imageUrl = null;  // will fetch the image url when finish upload user picture.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        progressDialog = TeamPathyDialogFactory.createProgressDialog(this);
        MyApp.createActivityComponent(this).inject(this);
        signUpPresenter.setSignUpView(this);
    }

    public void onSignUpClick(View view) {
        progressDialog.show();
        FirebaseInstanceId instnaceId = FirebaseInstanceId.getInstance();
        if (instnaceId != null && instnaceId.getToken() != null)
        {
            String pushNotificationToken = instnaceId.getToken();
            SignUp.Params params = new SignUp.Params(
                    accountEd.getText().toString(),
                    passwordEd.getText().toString(),
                    nameEd.getText().toString(),
                    imageUrl,
                    pushNotificationToken);

            Log.d("Push" ,  pushNotificationToken);
            if (checkInputAvailable(params))
                signUpPresenter.signUp(params);
        }
        else
            showErrorDialog();
    }

    private void showErrorDialog(){
        TeamPathyDialogFactory.networkErrorDialogBuilder(this).show();
    }

    private boolean checkInputAvailable(SignUp.Params params){
        return checkPasswordConfirm(params.getPassword(),
                passwordConfirmEd.getText().toString()) &&
        checkInputLength(params);
    }

    private boolean checkPasswordConfirm(String password, String confirm) {
        if ( !password.equals(confirm) )
        {
            passwordConfirmEd.setError(getString(R.string.password_not_conform));
            return false;
        }
        return true;
    }

    private boolean checkInputLength(SignUp.Params params) {
        boolean invalid;
        if ( invalid = photoUploadingProgresses.getVisibility() == View.VISIBLE)
            Toast.makeText(getApplicationContext(), R.string.photo_has_not_been_uploaded , Toast.LENGTH_SHORT).show();
        if ( invalid |= params.getAccount().length() < 7 )
            accountEd.setError(getString(R.string.account_length_must_longer_than_seven));
        if ( invalid |= params.getPassword().length() < 7)
            passwordEd.setError(getString(R.string.password_length_should_be_longer_than_7));
        return !invalid;
    }

    public void photoImgOnClick(View view){
        CameraCropperUtils.takeCamera(this);
    }

    @Override
    public void onSignUpSuccessfully(User user) {
        progressDialog.dismiss();
        MyApp.createUserComponent(this, user);
        startActivity(new Intent(this, BaseActivity.class));
    }

    @Override
    public void onImageUploadSuccessfully(String imageUrl) {
        this.imageUrl = imageUrl;
        photoUploadingProgresses.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), getString(R.string.photo_uploaded_complete), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onImageUploadError(Throwable err) {
        err.printStackTrace();
    }


    @Override
    public void onAccountDuplicated() {
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), getString(R.string.accountDuplicated), Toast.LENGTH_SHORT).show();
        accountEd.setError(getString(R.string.accountDuplicated));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_CAMERA || requestCode == PICK_FROM_GALLERY)
        {
            if (resultCode == RESULT_OK) {
                Uri photoUri = data.getData();

                Log.d("camera","回傳照片url: " + photoUri);
                CameraCropperUtils.cropPhotoActivity(this, photoUri, CropImageView.CropShape.OVAL);
            }
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)
            {
                GlideHelper.loadToCircularImage(this, photoImg, result.getUri().toString());
                startUploadingImage(result.getUri().getPath());
            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        else
            finish();
    }


    private void startUploadingImage(String imagePath){
        photoUploadingProgresses.setVisibility(View.VISIBLE);
        signUpPresenter.uploadImage(imagePath);
    }
}
