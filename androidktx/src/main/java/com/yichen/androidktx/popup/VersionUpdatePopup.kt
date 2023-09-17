package com.yichen.androidktx.popup

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yichen.androidktx.core.click
import com.yichen.androidktx.core.gone
import com.yichen.androidktx.databinding.PopupVersionUpdateBinding
import com.yichen.androidktx.util.CommonUpdateInfo
import com.yichen.xpopup.XPopup
import com.yichen.xpopup.core.CenterPopupView


class VersionUpdatePopup (context: Context, var updateInfo: CommonUpdateInfo, var onOkClick: ((url:String)->Unit)): CenterPopupView(context){
    private lateinit var binding: PopupVersionUpdateBinding

    override fun onCreate() {
        super.onCreate()
        binding = PopupVersionUpdateBinding
                .inflate(LayoutInflater.from(context), this, false)
        contentView =  binding.root
        binding.tvOk.setTextColor(XPopup.getPrimaryColor())
        binding.tvInfo.text = "${updateInfo.update_info}"
        if(updateInfo.force_update==true){
            binding.tvCancel.gone()
            binding.vv.gone()
        }
        binding.tvCancel.click { dismiss() }
        binding.tvOk.click {
            onOkClick(updateInfo.download_url?:"")
            dismiss()
        }
    }
}