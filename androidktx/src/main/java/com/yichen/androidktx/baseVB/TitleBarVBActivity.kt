package com.yichen.androidktx.baseVB

import android.view.View
import androidx.viewbinding.ViewBinding
import com.yichen.androidktx.core.click
import com.yichen.androidktx.core.gone
import com.yichen.androidktx.core.visible
import com.yichen.androidktx.databinding.KtxActivityTitlebarBinding
import com.yichen.androidktx.util.ViewBindingCreator

abstract class TitleBarVBActivity<VB:ViewBinding> : AdaptVBActivity<KtxActivityTitlebarBinding>(){

    protected val childBinding by lazy {
        ViewBindingCreator.createViewBinding<VB>(
            this.javaClass,
            layoutInflater
        )!!
    }
    override fun initView() {
        binding.flBody.addView(childBinding.root)
        binding.titleBar.leftImageView().click { finish() }
    }


    override val mClass: Class<*>
        get() = Class.forName("com.yichen.androidktx.base.TitleBarActivity")

    fun setStatusBarColor(color: Int) = binding.fakeTitleBar.setBackgroundColor(color)

    fun titleBar() = binding.titleBar

    fun hideTitleDivider() = binding.titleDivider.gone()



    fun hideTitleBar(){
        titleBar().gone()
        binding.fakeTitleBar.gone()
        binding.titleDivider.gone()
    }
    fun hideTitleBarWithMarginTop(){
        titleBar().gone()
        binding.titleDivider.gone()
    }

    fun showTitleBar(){
        titleBar().visible()
        binding.fakeTitleBar.visible()
        binding.titleDivider.visible()
    }
}