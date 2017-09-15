package com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository;

import com.ood.clean.waterball.teampathy.Domain.Repository.ImageUploadRepository;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@Singleton
public class ImgurRepository implements ImageUploadRepository {
    private ImgurAPI imgurApi;

    @Inject
    public ImgurRepository(Retrofit retrofit) {
        this.imgurApi = retrofit.create(ImgurAPI.class);
    }

    @Override
    public String uploadImage(File imageFile) throws Exception {
        RequestBody request = RequestBody.create(MediaType.parse("image/*"), imageFile);
        Call<ImageResponse> call =  imgurApi.postImage(request);
        return call.execute().body().data.link;
    }

    public interface ImgurAPI {
        String SERVER = "https://api.imgur.com";
        public static final String AUTH = "6676c6a29041d49";

        @Headers("Authorization: Client-ID " + AUTH)
        @POST("/3/upload")
        Call<ImageResponse> postImage(
                @Body RequestBody image
        );
    }

    public static class ImageResponse {

        public boolean success;
        public int status;
        public ImageResponse.UploadedImage data;

        public static class UploadedImage{
            public String id;
            public String title;
            public String description;
            public String type;
            public boolean animated;
            public int width;
            public int height;
            public int size;
            public int views;
            public int bandwidth;
            public String vote;
            public boolean favorite;
            public String account_url;
            public String deletehash;
            public String name;
            public String link;

            @Override public String toString() {
                return "UploadedImage{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", description='" + description + '\'' +
                        ", type='" + type + '\'' +
                        ", animated=" + animated +
                        ", width=" + width +
                        ", height=" + height +
                        ", size=" + size +
                        ", views=" + views +
                        ", bandwidth=" + bandwidth +
                        ", vote='" + vote + '\'' +
                        ", favorite=" + favorite +
                        ", account_url='" + account_url + '\'' +
                        ", deletehash='" + deletehash + '\'' +
                        ", name='" + name + '\'' +
                        ", link='" + link + '\'' +
                        '}';
            }
        }

        @Override public String toString() {
            return "ImageResponse{" +
                    "success=" + success +
                    ", status=" + status +
                    ", data=" + data.toString() +
                    '}';
        }
    }
}
