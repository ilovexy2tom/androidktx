package com.yichen.androidktx.qrcode

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.ViewUtils
import com.yichen.androidktx.core.PermissionExt

/**
 * 二维码/条形码工具类
 */
object QrCodeUtil {
    var requestCode = 1


    /**
     * 开启二维码扫描界面
     */
    fun start(
        source: Any,
        reqCode: Int = 1,
        color: Int? = null,
        launcher: ActivityResultLauncher<Intent>
    ) {
        requestCode = reqCode
        PermissionExt.applyOnlyCameraPermission(PermissionExt.getContext(source)) {
            if (source is AppCompatActivity) {
                val intent = Intent(source, QrCodeActivity::class.java)
                if (color != null) intent.putExtra("color", color)
                launcher.launch(intent)


            } else if (source is Fragment) {
                val intent = Intent(source.context, QrCodeActivity::class.java)
                if (color != null) intent.putExtra("color", color)
                launcher.launch(intent)
            }
        }

    }

    /**
     * 生成二维码
     */
    fun genQrCode(
        content: String,
        size: Int = ConvertUtils.dp2px(200f),
        logo: Bitmap? = null,
        foregroundColor: Int = Color.BLACK,
        onFinish: ((Bitmap) -> Unit)
    ) {
        ThreadUtils.getIoPool().execute {
            val bitmap = if (logo != null) {
                QRCodeEncoder.syncEncodeQRCode(content, size, foregroundColor, logo)
            } else {
                QRCodeEncoder.syncEncodeQRCode(content, size, foregroundColor)
            }
            ViewUtils.runOnUiThread {
                onFinish(bitmap)
            }
        }
    }

    /**
     * 从Bitmap图片解析出二维吗
     */
    fun parseQrCode(bitmap: Bitmap, onFinish: (String) -> Unit) {
        ThreadUtils.getIoPool().execute {
            onFinish(QRCodeDecoder.syncDecodeQRCode(bitmap))
        }
    }

    /**
     * 从图片路径解析出二维吗
     */
    fun parseQrCode(imgPath: String, onFinish: (String) -> Unit) {
        ThreadUtils.getIoPool().execute {
            onFinish(QRCodeDecoder.syncDecodeQRCode(imgPath))
        }
    }

    /**
     * 生成条形码
     */
    fun genBarCode(
        content: String,
        width: Int = ConvertUtils.dp2px(200f),
        height: Int = ConvertUtils.dp2px(100f),
        textSize: Int = 0,
        onFinish: ((Bitmap) -> Unit)
    ) {
        ThreadUtils.getIoPool().execute {
            val bitmap = QRCodeEncoder.syncEncodeBarcode(content, width, height, textSize)
            onFinish(bitmap)
        }
    }

    /**
     * 获取扫描结果
     */
    fun fetchResult(data: Intent?): String {
        if (data != null) {
            return data.getStringExtra("result") ?: ""
        }
        return ""
    }
}