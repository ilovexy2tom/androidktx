package com.yichen.androidktx.imageSelector;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.utils.ActivityCompatHelper;
import com.yichen.androidktx.R;


/**
 * @author：luck
 * @date：2019-11-13 17:02
 * @describe：Glide加载引擎
 */
public class GlideEngine implements ImageEngine {

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, String url, int maxWidth, int maxHeight) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .override(maxWidth, maxHeight)
                .into(imageView);
    }

    @Override
    public void loadAlbumCover(Context context, String url, ImageView imageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .override(200, 200)
                .centerCrop()
                .placeholder(R.drawable.ps_image_placeholder)
                .into(imageView);
    }


    /**
     * 加载gif
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */

    public void loadAsGifImage(@NonNull Context context, @NonNull String url,
                               @NonNull ImageView imageView) {
        Glide.with(context)
                .asGif()
                .load(url)
                .into(imageView);
    }

    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadGridImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(url)
                .override(200, 200)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void pauseRequests(Context context) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }
        Glide.with(context).pauseRequests();
    }

    @Override
    public void resumeRequests(Context context) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return;
        }
        Glide.with(context).resumeRequests();
    }


    private GlideEngine() {
    }

    private static GlideEngine instance;

    public static GlideEngine createGlideEngine() {
        if (null == instance) {
            synchronized (GlideEngine.class) {
                if (null == instance) {
                    instance = new GlideEngine();
                }
            }
        }
        return instance;
    }
}
