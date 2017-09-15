package com.ood.clean.waterball.teampathy.MyUtils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class CameraCropperUtils {
    public static final int PICK_FROM_CAMERA = 1001;  //拍照需求碼
    public static final int PICK_FROM_GALLERY = 1000;  //挑選照片需求碼

    public static void takeCamera(AppCompatActivity activity){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED )
                activity.startActivityForResult(intent,PICK_FROM_CAMERA);
            else
                ActivityCompat.requestPermissions(activity,
                        new String[] { Manifest.permission.CAMERA , Manifest.permission.READ_EXTERNAL_STORAGE} ,
                        PICK_FROM_CAMERA
                );
        }
        else
            activity.startActivityForResult(intent,PICK_FROM_CAMERA);
    }

    public static void pickFromGallery(AppCompatActivity activity){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED )
                activity.startActivityForResult(intent, PICK_FROM_GALLERY);
            else
                ActivityCompat.requestPermissions(activity,
                        new String[] { Manifest.permission.READ_EXTERNAL_STORAGE},
                        PICK_FROM_GALLERY
                );
        }
        else
            activity.startActivityForResult(intent, PICK_FROM_GALLERY);
    }

    public static void cropPhotoActivity(AppCompatActivity activity, Uri imageUri , CropImageView.CropShape cropShape){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED )
                CropImage.activity(imageUri)
                        .setCropShape(cropShape)
                        .setFixAspectRatio(true)
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(activity);
            else
                ActivityCompat.requestPermissions(activity,
                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE
                );
        }
        else
            CropImage.activity(imageUri)
                    .setCropShape(cropShape)
                    .setFixAspectRatio(true)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(activity);
    }

}
