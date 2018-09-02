package com.zwonline.top28.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


    /**
     * 描述：轮播图
     * @author YSG
     * @date 2018/1/16
     */
public class BannerImage extends com.youth.banner.loader.ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }
}