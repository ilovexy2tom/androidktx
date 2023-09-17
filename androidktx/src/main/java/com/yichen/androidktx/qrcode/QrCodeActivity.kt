package com.yichen.androidktx.qrcode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.blankj.utilcode.util.ToastUtils
import com.yichen.androidktx.R
import com.yichen.androidktx.core.click
import com.yichen.androidktx.core.color
import com.yichen.androidktx.databinding.KtxActivityQrCodeBinding

class QrCodeActivity : AppCompatActivity(), QRCodeView.Delegate {
    private lateinit var binding: KtxActivityQrCodeBinding
    var openFlash = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = KtxActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.zxingView.setDelegate(this)
        var color = intent.getIntExtra("color", color(R.color.colorPrimary))
        binding.zxingView.scanBoxView.cornerColor = color
        binding.zxingView.scanBoxView.borderColor = color
//        zxingView.scanBoxView.scanLineColor = color  //无效
        binding.btnBack.click { finish() }
        binding.btnFlash.click {
            if(openFlash){
                binding.zxingView.closeFlashlight()
            }else{
                binding.zxingView.openFlashlight()
            }
            openFlash = !openFlash
            binding.btnFlash.setImageResource(if(openFlash) R.mipmap._ktx_flash_open else R.mipmap._ktx_flash_close)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.zxingView.startSpotAndShowRect()
    }

    override fun onStop() {
        binding.zxingView.stopCamera()
        super.onStop()
    }

    override fun onDestroy() {
        binding.zxingView.onDestroy()
        super.onDestroy()
    }

    override fun onScanQRCodeSuccess(result: String?) {
        val intent = Intent()
        intent.putExtra("result", result)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
    }

    override fun onScanQRCodeOpenCameraError() {
        ToastUtils.showLong("打开相机出错")
    }
}