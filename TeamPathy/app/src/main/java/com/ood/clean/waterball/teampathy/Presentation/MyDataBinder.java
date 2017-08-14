package com.ood.clean.waterball.teampathy.Presentation;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ood.clean.waterball.teampathy.MyUtils.GlideHelper;

/*
* The binder to help Android data binding.
 */
public final class MyDataBinder {

    private MyDataBinder() {
        //NO-OP
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).fitCenter().into(view);
    }

    @BindingAdapter("circleImageUrl")
    public static void loadCircleImage(ImageView view, String url) {
        GlideHelper.loadToCircularImage(view.getContext(),view,url);
    }
}