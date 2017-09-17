package com.ood.clean.waterball.teampathy.Presentation.Interfaces;


import android.net.Uri;

public interface TakePhotoView {
    void onTakePhotoResult(Uri photoUri);
    void onCropPhotoResult(Uri photoUri);
    void onPhotoUploaded(String imageUrl);
}
