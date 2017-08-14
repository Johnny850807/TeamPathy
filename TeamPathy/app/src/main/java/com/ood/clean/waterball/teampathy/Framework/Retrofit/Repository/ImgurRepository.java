package com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository;

import com.ood.clean.waterball.teampathy.Domain.Repository.ImageUploadRepository;

import java.io.File;

import javax.inject.Singleton;

@Singleton
public class ImgurRepository implements ImageUploadRepository {

    @Override
    public String uploadImage(File imageFile) throws Exception {
        //todo imgur logic
        return null;
    }
}
