package com.ood.clean.waterball.teampathy.MyUtils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import com.ood.clean.waterball.teampathy.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class CameraCropperUtils {
    public static final int PICK_FROM_CAMERA = 1001;  //拍照需求碼
    public static final int PICK_FROM_GALLERY = 1000;  //挑選照片需求碼

    public static void takeCamera(Activity activity){
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

    public static void pickFromGallery(Activity activity){
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

    public static void cropPhotoActivity(Activity activity, Uri imageUri , CropImageView.CropShape cropShape){

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

    public static void createAndShowDialogForTakePhotoOptions(final Activity activity){
        String[] options = activity.getResources().getStringArray(R.array.take_picture_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, options);
        new AlertDialog.Builder(activity)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        switch (position)
                        {
                            case 0:
                                takeCamera(activity);
                                break;
                            case 1:
                                pickFromGallery(activity);
                        }
                    }
                })
                .show();
    }

}
