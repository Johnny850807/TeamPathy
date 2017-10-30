package com.ood.clean.waterball.teampathy.Presentation.UI.Activity;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.UserScope;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.CameraCropperUtils;
import com.ood.clean.waterball.teampathy.MyUtils.PageController;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.BasePresenter;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.TakePhotoView;
import com.ood.clean.waterball.teampathy.Presentation.UI.Fragment.ProjectsFragment;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.NavHeaderBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ood.clean.waterball.teampathy.MyUtils.CameraCropperUtils.PICK_FROM_CAMERA;
import static com.ood.clean.waterball.teampathy.MyUtils.CameraCropperUtils.PICK_FROM_GALLERY;

@UserScope
public class BaseActivity extends AppCompatActivity implements BasePresenter.BaseView{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progressbar) ProgressBar progressBar;
    @BindView(R.id.drawerlayout) DrawerLayout drawlayout;
    @BindView(R.id.navigationView) NavigationView navigationView;
    @Inject User user;
    PageController pageController =  new PageControllerImp();
    ProgressDialog progressDialog;

    /* When one task comes in, number plus 1, minus 1 if one done .
    *  Used for checking if all tasks are done ( = 0),
    *  if all tasks done, then the progressbar or dialog can be hided.
    * */
    int taskLoadingForBar = 0;
    int taskLoadingForDialog = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_page);
        ButterKnife.bind(this);
        MyApp.getUserComponent(this).inject(this);
        setupViews();
    }

    private void setupViews() {
        setSupportActionBar(toolbar);
        progressDialog = TeamPathyDialogFactory.createProgressDialog(this);
        loadContainer();
        setupDrawer();
    }

    private void loadContainer() {
        pageController.changePage(new ProjectsFragment());
    }

    private void setupDrawer() {
        setupNavigationView();
        setupActionBarDrawerToggle();
    }

    private void setupNavigationView() {
        NavHeaderBinding navbind = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.nav_header, navigationView, false);
        navigationView.addHeaderView(navbind.getRoot());
        navbind.setUser(user);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_homepage_nav:
                        pageController.changePage(new ProjectsFragment());
                        break;
                    case R.id.profile_nav:
                        break;
                    case R.id.whole_timeline_nav:
                        break;
                    case R.id.log_out_nav:
                        finish();
                }
                drawlayout.closeDrawers();
                return true;
            }
        });
    }

    private void setupActionBarDrawerToggle() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawlayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void setToolbarTitle(String title) {
        setTitle(title);
    }

    @Override
    public void showProgressBar() {
        taskLoadingForBar ++;
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        taskLoadingForBar = taskLoadingForBar - 1 <= 0 ? 0 : taskLoadingForBar - 1;
        if (taskLoadingForBar == 0)
            progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressDialog() {
        taskLoadingForDialog ++;
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        taskLoadingForDialog = taskLoadingForDialog - 1 <= 0 ? 0 : taskLoadingForDialog - 1;
        if (taskLoadingForDialog == 0)
            progressDialog.dismiss();
    }

    @Override
    public PageController getPageController() {
        return pageController;
    }

    @Override
    public void createAndShowDialogForOptionsOfTakePicture() {
        CameraCropperUtils.createAndShowDialogForTakePhotoOptions(this);
    }


    private class PageControllerImp extends PageController {

        public PageControllerImp() {
            super(BaseActivity.this, BaseActivity.this.getSupportFragmentManager(),
                    R.id.container);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Transition getEnterTransition() {
            return TransitionInflater.from(context).inflateTransition(R.transition.auto_transition);

        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Transition getExitTransition() {
            return TransitionInflater.from(context).inflateTransition(R.transition.auto_transition);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_CAMERA || requestCode == PICK_FROM_GALLERY)
        {
            if (resultCode == RESULT_OK)
                sendPhotoUriToAllPhotoViews(new PhotoViewAction() {
                    @Override
                    public void invoke(TakePhotoView takePhotoView) {
                        takePhotoView.onTakePhotoResult(data.getData());
                    }
                });
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
                sendPhotoUriToAllPhotoViews(new PhotoViewAction() {
                    @Override
                    public void invoke(TakePhotoView takePhotoView) {
                        takePhotoView.onCropPhotoResult(result.getUri());
                    }
                });

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendPhotoUriToAllPhotoViews(PhotoViewAction action){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        for (Fragment fragment : fragments)
            if (fragment instanceof TakePhotoView)
            {
                TakePhotoView photoView = (TakePhotoView) fragment;
                action.invoke(photoView);
            }
    }

    interface PhotoViewAction{
        void invoke(TakePhotoView takePhotoView);
    }
}
