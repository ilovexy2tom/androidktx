package com.yichen.androidktx.core

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.yichen.androidktx.AndroidKTX
import com.yichen.androidktx.util.GlideBlurTransformation
import com.yichen.androidktx.util.SuperGlideTransformation
/**
 * Description: ImageView相关
 * Create by yichen, at 2018/12/5
 */

/**
 * Glide加载图片
 * @param url 可以是网络，可以是File，可以是资源id等等Glide支持的类型
 * @param placeholder 默认占位图
 * @param error 失败占位图
 * @param borderSize 边框粗细
 * @param borderColor 边框颜色
 * @param isCircle 是否是圆形，默认false，注意：isCircle和roundRadius两个只能有一个生效
 * @param isCenterCrop 是否设置scaleType为CenterCrop，你也可以在布局文件中设置
 * @param roundRadius 圆角角度，默认为0，不带圆角，注意：isCircle和roundRadius两个只能有一个生效
 * @param roundArray 圆角Array:[lt,rt, rb, lb]
 * @param isCrossFade 是否有过渡动画，默认没有过渡动画
 * @param isForceOriginalSize 是否强制使用原图，默认false
 */
@SuppressLint("CheckResult")
@JvmOverloads
fun ImageView.load(
    url: Any?, placeholder: Int = 0, error: Int = 0,
    isCircle: Boolean = false,
    isCenterCrop: Boolean = false,
    blurScale: Float = 0f,
    blurRadius: Float = 20f,
    roundRadius: Int = 0,
    isCrossFade: Boolean = false,
    skipMemoryCache: Boolean = false,
    isForceOriginalSize: Boolean = false,
    targetWidth: Int = 0,
    targetHeight: Int = 0,
    onImageLoad: ((resource: Drawable?) -> Unit)? = null,
    onImageFail: (() -> Unit)? = null,
) {
//    if (url is String && !url.startsWith("http") && url.length > 0){
//        load(placeholder)
//        return
//    }
    if (context == null) return
    if (context is Activity && ((context as Activity).isDestroyed || (context as Activity).isFinishing)) return
    val options = RequestOptions()
        .placeholder(placeholder)
        .error(error)
        .skipMemoryCache(skipMemoryCache)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .apply {
            if (isCenterCrop && scaleType != ImageView.ScaleType.CENTER_CROP)
                scaleType = ImageView.ScaleType.CENTER_CROP
            if (isCircle) {
                transform(CircleCrop())
            } else if (roundRadius != 0) {
                if (isCenterCrop) {
                    transform(CenterCrop(), RoundedCorners(roundRadius))
                } else {
                    transform(RoundedCorners(roundRadius))
                }
            }
            if (blurScale > 0) transform(
                GlideBlurTransformation(
                    scale = blurScale,
                    blurRadius = blurRadius
                )
            )
            if (isForceOriginalSize) {
                override(Target.SIZE_ORIGINAL)
            }
            if (targetWidth != 0 && targetHeight != 0) {
                override(targetWidth, targetHeight)
            }
        }
    val glide = Glide.with(context)
        .load(if (url is String && url.isNotEmpty()) ExpiresGlideUrl(url) else url)
        .apply(options)
        .apply {
            if (isCrossFade) transition(DrawableTransitionOptions.withCrossFade())
            if (onImageLoad != null || onImageFail != null) {
                listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        onImageFail?.invoke()
                        return onImageFail != null
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
//                        if (resource is GifDrawable) {
//                            playOnceGif(resource, onGiftOnce)
//                        }
                        onImageLoad?.invoke(resource)
                        return false
                    }
                })
            }

        }
    glide.into(this)
}

fun String.preloadImage(onSuccess: ((Drawable?) -> Unit)? = null, onFail: (() -> Unit)? = null) {
    if (this.isEmpty() || this.isBlank()) return
    Glide.with(AndroidKTX.context)
        .load(ExpiresGlideUrl(this))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onFail?.invoke()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onSuccess?.invoke(resource)
                return false
            }
        })
        .preload()
}

/**
 * 缓存key 更换
 */
class ExpiresGlideUrl(val url: String?) : GlideUrl(url) {
    override fun getCacheKey(): String? {
        return if (url.isNullOrEmpty()) {
            super.getCacheKey()
        } else {
            url.replace(findExpiresParam(), "")
        }
    }

    private fun findExpiresParam(): String {


        var expiresParam = ""
        val expiresKeyIndex: Int? = url?.indexOf("?Expires=")
        expiresKeyIndex?.let {
            if (expiresKeyIndex != -1) {
                expiresParam = url?.substring(expiresKeyIndex).orEmpty()
            }
        }

//        LogUtils.dTag("ExpiresGlideUrl", "param = $expiresParam")
        return expiresParam
    }
}


