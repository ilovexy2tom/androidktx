package com.yichen.androidktx.core

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

object PermissionExt {

    //录制和选择视频
     fun applyVideoPermission(context: Context,agree:()->Unit){
        val preMission =
            arrayListOf(
                Permission.READ_MEDIA_VIDEO,
                Permission.RECORD_AUDIO,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.CAMERA
            )
        if (XXPermissions.isGranted(context, preMission)) {
            agree.invoke()
        }else{
            XXPermissions.with(context).permission(preMission)
                .request(object : OnPermissionCallback {
                    override fun onGranted(
                        permissions: MutableList<String>,
                        all: Boolean
                    ) {
                        if (all) {
                            agree.invoke()
                        }
                    }

                    override fun onDenied(
                        permissions: MutableList<String>,
                        never: Boolean
                    ) {
                        super.onDenied(permissions, never)
                        ToastUtils.showShort("Failed to obtain permission, unable to shoot")
                    }


                })
        }
    }

    fun applyOnlyCameraPermission(context: Context,agree:()->Unit){
        val preMission =
            arrayListOf(
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.CAMERA
            )
        if (XXPermissions.isGranted(context, preMission)) {
            agree.invoke()
        }else{
            XXPermissions.with(context).permission(preMission)
                .request(object : OnPermissionCallback {
                    override fun onGranted(
                        permissions: MutableList<String>,
                        all: Boolean
                    ) {
                        if (all) {
                            agree.invoke()
                        }
                    }

                    override fun onDenied(
                        permissions: MutableList<String>,
                        never: Boolean
                    ) {
                        super.onDenied(permissions, never)
                        ToastUtils.showShort("Permission denied, unable to use the scan function")
                    }


                })
        }
    }

    fun getContext(from:Any):Context{
        return if (from is Activity) from else (from as Fragment).requireContext()
    }

    //拍摄和选择相册
    fun applyImagePermission(context: Context, agree:()->Unit){
        val preMission =
            arrayListOf(
                Permission.READ_MEDIA_IMAGES,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.CAMERA
            )
        if (XXPermissions.isGranted(context, preMission)) {
            agree.invoke()
        }else{
            XXPermissions.with(context).permission(preMission)
                .request(object : OnPermissionCallback {
                    override fun onGranted(
                        permissions: MutableList<String>,
                        all: Boolean
                    ) {
                        if (all) {
                            agree.invoke()
                        }
                    }

                    override fun onDenied(
                        permissions: MutableList<String>,
                        never: Boolean
                    ) {
                        super.onDenied(permissions, never)
                        ToastUtils.showShort("Failed to obtain permission, unable to use the photo album function")
                    }


                })
        }
    }
}