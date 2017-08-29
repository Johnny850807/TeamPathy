package com.ood.clean.waterball.teampathy.Presentation.UI.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ActivityScope;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.UseCase.User.SignUp;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.SignUpPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.SignUpPresenterImp;
import com.ood.clean.waterball.teampathy.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@ActivityScope
public class SignUpActivity extends AppCompatActivity implements SignUpPresenter.SignUpView{
    @Inject SignUpPresenterImp signUpPresenter;
    @BindView(R.id.nameEd)EditText nameEd;
    @BindView(R.id.accountEd)EditText accountEd;
    @BindView(R.id.passwordEd)EditText passwordEd;
    @BindView(R.id.passwordConfirmEd)EditText passwordConfirmEd;

    ProgressDialog progressDialog;
    String imageUrl;  // will fetch the image url when finish upload user picture.

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
        if ( invalid = params.getAccount().length() < 7 )
            accountEd.setError(getString(R.string.account_length_must_longer_than_seven));
        if ( invalid |= params.getPassword().length() < 7)
            passwordEd.setError(getString(R.string.password_length_should_be_longer_than_7));
        return !invalid;
    }



    @Override
    public void onSignUpSuccessfully(User user) {
        progressDialog.dismiss();
        MyApp.createUserComponent(this, user);
        startActivity(new Intent(this, BaseActivity.class));
    }


    @Override
    public void onAccountDuplicated() {
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), getString(R.string.accountDuplicated), Toast.LENGTH_SHORT).show();
        accountEd.setError(getString(R.string.accountDuplicated));
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}
