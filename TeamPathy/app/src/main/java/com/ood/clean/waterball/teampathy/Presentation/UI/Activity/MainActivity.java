package com.ood.clean.waterball.teampathy.Presentation.UI.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ActivityScope;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.SharedPreferencesHelper;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.MainPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.MainPresenterImp;
import com.ood.clean.waterball.teampathy.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@ActivityScope
public class MainActivity extends AppCompatActivity implements MainPresenter.MainView{
    @Inject MainPresenterImp mainPresenterImp;
    @BindView(R.id.inputAccount_Main) EditText accountTxt;
    @BindView(R.id.inputPassword_Main) EditText passwordTxt;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferencesHelper.init(this);
        ButterKnife.bind(this);
        progressDialog = TeamPathyDialogFactory.createProgressDialog(this);
        MyApp.createActivityComponent(this).inject(this);
        mainPresenterImp.setMainView(this);
    }

    public void logInOnClickMain(View view) {
        progressDialog.show();
        String account = accountTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        mainPresenterImp.signIn(account,password);
    }

    @Override
    public void signInSuccessfully(User user) {
        MyApp.createUserComponent(this,user);
        progressDialog.dismiss();
        startActivity(new Intent(this,BaseActivity.class));
    }

    @Override
    public void onUserNotFound() {
        progressDialog.dismiss();
        TeamPathyDialogFactory.templateBuilder(this)
                .setTitle("登入錯誤")
                .setMessage("帳號或密碼錯誤，請再輸入一次。")
                .setPositiveButton(R.string.confirm, null)
                .show();
    }

    @Override
    public void onPushNotificationNotPrepared() {
        TeamPathyDialogFactory.networkErrorDialogBuilder(this).show();
    }

    @Override
    public void onOperationTimeOut(Throwable err) {
        TeamPathyDialogFactory.networkErrorDialogBuilder(this).setMessage(err.getMessage()).show();
    }

    public void registerOnClick(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
