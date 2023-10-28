package com.yichen.androidktx.imageSelector

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.luck.picture.lib.animators.AnimationType
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.language.LanguageConfig
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File

/**
 * @author by Dell
 * @date on 2022/12/4
 * @describe
 */
object ImagePicker {

    fun simplerSelector(
        any: Any,
        type: Int = SelectMimeType.ofImage(),
        maxSelectNum: Int = 1,
        minSelectNum: Int = 1,
        maxVideoSelectNum: Int = 1,
        imageSpanCount: Int = if (type == SelectMimeType.ofVideo()) 2 else 3,
        onResult: (MutableList<LocalMedia>?) -> Unit
    ) {
        val selector = if (any is AppCompatActivity) {
            PictureSelector.create(any)
        } else {
            any as Fragment
            PictureSelector.create(any)
        }
        selector
            .openGallery(type) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .setImageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
//            .setPictureWindowAnimationStyle(PictureWindowAnimationStyle.ofDefaultWindowAnimationStyle()) // 自定义相册启动退出动画
            .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION) // 列表动画效果
            .isMaxSelectEnabledMask(true) // 选择数到了最大阀值列表是否启用蒙层效果
            .setMaxSelectNum(maxSelectNum)
            .setLanguage(LanguageConfig.ENGLISH)
            .isWithSelectVideoImage(true)
            .setMaxVideoSelectNum(maxVideoSelectNum) // 视频最大选择数量
            .setMinVideoSelectNum(minSelectNum) // 最小选择数量
            .setImageSpanCount(imageSpanCount) // 每行显示个数
            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) // 设置相册Activity方向，不设置默认使用系统
            .setSelectionMode(SelectModeConfig.SINGLE) // 多选 or 单选
            .isPreviewImage(true) // 是否可预览图片
            .isDisplayCamera(true) // 是否显示拍照按钮
            .setCameraImageFormat(PictureMimeType.PNG) // 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
            .setCompressEngine(CompressFileEngine { context, source, call -> // 是否压缩
                Luban.with(context).load(source).ignoreBy(100)
                    .setCompressListener(object : OnNewCompressListener {
                        override fun onStart() {
                        }
                        override fun onSuccess(source: String?, compressFile: File?) {
                            call?.onCallback(source, compressFile?.getAbsolutePath())
                        }

                        override fun onError(source: String?, e: Throwable?) {
                            call?.onCallback(source, null)
                        }
                    }).launch();

            })
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    onResult.invoke(result)
                }

                override fun onCancel() {}
            })
    }

    private fun startSelector(
        any: Any,
        type: Int = SelectMimeType.ofImage(),
        maxSelectNum: Int = 1,
        minSelectNum: Int = 1,
        maxVideoSelectNum: Int = 1,
        imageSpanCount: Int = if (type == SelectMimeType.ofVideo()) 2 else 3,
        onResult: (MutableList<LocalMedia>?) -> Unit
    ) {
        val selector = if (any is AppCompatActivity) {
            PictureSelector.create(any)
        } else {
            any as Fragment
            PictureSelector.create(any)
        }
        selector
            .openGallery(type) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .setImageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
//            .setPictureWindowAnimationStyle(PictureWindowAnimationStyle.ofDefaultWindowAnimationStyle()) // 自定义相册启动退出动画
            .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION) // 列表动画效果
            .isMaxSelectEnabledMask(true) // 选择数到了最大阀值列表是否启用蒙层效果
            .setMaxSelectNum(maxSelectNum)
            .isWithSelectVideoImage(true)
            .setMaxVideoSelectNum(maxVideoSelectNum) // 视频最大选择数量
            .setMinVideoSelectNum(minSelectNum) // 最小选择数量
            .setImageSpanCount(imageSpanCount) // 每行显示个数
            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) // 设置相册Activity方向，不设置默认使用系统
            .setSelectionMode(SelectModeConfig.SINGLE) // 多选 or 单选
            .isPreviewImage(true) // 是否可预览图片
            .isDisplayCamera(true) // 是否显示拍照按钮
            .setCameraImageFormat(PictureMimeType.PNG) // 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
            .setCompressEngine(CompressFileEngine { context, source, call -> // 是否压缩
                Luban.with(context).load(source).ignoreBy(100)
                    .setCompressListener(object : OnNewCompressListener {
                        override fun onStart() {
                        }
                        override fun onSuccess(source: String?, compressFile: File?) {
                            call?.onCallback(source, compressFile?.getAbsolutePath())
                        }

                        override fun onError(source: String?, e: Throwable?) {
                            call?.onCallback(source, null)
                        }
                    }).launch();

            })
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    onResult.invoke(result)
                }

                override fun onCancel() {}
            })
    }

    fun fetchResult(localMedia: LocalMedia?): String {
        return localMedia?.compressPath?.ifEmpty { localMedia.realPath.orEmpty() }
            ?: localMedia?.realPath.orEmpty()
    }
}