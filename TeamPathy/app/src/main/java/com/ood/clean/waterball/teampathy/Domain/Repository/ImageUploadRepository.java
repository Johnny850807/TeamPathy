package com.ood.clean.waterball.teampathy.Domain.Repository;


import java.io.File;

public interface ImageUploadRepository {
    public String uploadImage(File imageFile) throws Exception;
}
