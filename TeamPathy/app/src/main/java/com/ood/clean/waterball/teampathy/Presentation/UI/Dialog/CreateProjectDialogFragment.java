package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.CameraCropperUtils;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.TakePhotoView;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.ProjectsPresenterImp;
import com.ood.clean.waterball.teampathy.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateProjectDialogFragment extends MakeSureToCancelBaseDialogFragment implements TakePhotoView{
    @BindView(R.id.titleEd) TextInputEditText nameEd;
    @BindView(R.id.categoryEd) TextInputEditText categoryEd;
    @BindView(R.id.descriptionEd) TextInputEditText descriptionEd;
    @BindView(R.id.passwordEd) TextInputEditText passwordEd;
    @BindView(R.id.passwordChbx) CheckBox passwordChb;
    @BindView(R.id.takePhotoBtn) ImageButton takePhotoBtn;
    @BindView(R.id.photoUploadingProgress) ProgressBar photoUploadingProgress;
    @Inject ProjectsPresenterImp presenterImp;
    String imageUrl;

    //todo imageUrl should be fetched from the picture the user uploads instead of the null parameter.

    @Override
    protected Dialog onCustomDialogSetting(AlertDialog alertDialog) {
        alertDialog.setTitle(R.string.create_a_new_project);
        return alertDialog;
    }

    @Override
    protected View onViewCreated() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_project_dialog,null);
        bind(view);
        return view;
    }

    private void bind(View view) {
        ButterKnife.bind(this,view);
        MyApp.getUserComponent(getActivity()).inject(this);
        presenterImp.setTakePhotoView(this);
    }

    @Override
    protected DialogInterface.OnClickListener getOnPositiveButtonClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEd.getText().toString();
                String type = categoryEd.getText().toString();
                boolean hasPassword = passwordChb.isChecked();
                String password = hasPassword ? passwordEd.getText().toString() : Project.NO_PASSWORD;
                String description = descriptionEd.getText().toString();
                if (isAvailable(name,type,description))
                {
                    presenterImp.create(new Project(name,type,description,imageUrl,password));
                    getBaseView().showProgressDialog();
                }
            }
        };
    }

    @OnClick(R.id.takePhotoBtn)
    public void takePhotoOnClick(View view){
        CameraCropperUtils.createAndShowDialogForTakePhotoOptions(getActivity());
    }

    private boolean isAvailable(String name, String type, String description) {
        boolean error;
        if ( error = name.isEmpty() )
            nameEd.setError(getContext().getString(R.string.please_input_project_title));
        if ( error |= photoUploadingProgress.getVisibility() == View.VISIBLE)
            Toast.makeText(getActivity(), getString(R.string.photo_has_not_been_uploaded), Toast.LENGTH_SHORT).show();
        if ( error |= type.isEmpty() )
            categoryEd.setError(getContext().getString(R.string.please_input_project_type));
        if ( error |= description.isEmpty() )
            descriptionEd.setError(getContext().getString(R.string.please_input_project_description));
        return !error;
    }

    @Override
    public void onTakePhotoResult(Uri photoUri) {
        Log.d("camera","回傳照片url: " + photoUri);
        CameraCropperUtils.cropPhotoActivity(getActivity(), photoUri, CropImageView.CropShape.RECTANGLE);
    }

    @Override
    public void onCropPhotoResult(Uri photoUri) {
        startUploadingImage(photoUri.getPath());
        Glide.with(this).load(photoUri.toString()).centerCrop().into(takePhotoBtn);
    }

    @Override
    public void onPhotoUploaded(String imageUrl) {
        this.imageUrl = imageUrl;
        photoUploadingProgress.setVisibility(View.GONE);
        Toast.makeText(getActivity(), getString(R.string.photo_uploaded_complete), Toast.LENGTH_SHORT).show();
    }

    private void startUploadingImage(String imagePath){
        photoUploadingProgress.setVisibility(View.VISIBLE);
        presenterImp.uploadPhoto(imagePath);
    }
}
