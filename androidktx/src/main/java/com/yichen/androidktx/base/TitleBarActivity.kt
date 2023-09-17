package com.yichen.androidktx.base

import android.view.View
import com.yichen.androidktx.R
import com.yichen.androidktx.core.click
import com.yichen.androidktx.core.gone
import com.yichen.androidktx.core.visible
import com.yichen.androidktx.databinding.KtxActivityTitlebarBinding

abstract class TitleBarActivity : AdaptActivity<KtxActivityTitlebarBinding>(){


    override fun initView() {
        binding.flBody.addView(View.inflate(this, getBodyLayout(), null))
        binding.titleBar.leftImageView().click { finish() }
    }


    override val mClass: Class<*>
        get() = Class.forName("com.yichen.androidktx.base.TitleBarActivity")

    fun setStatusBarColor(color: Int) = binding.fakeTitleBar.setBackgroundColor(color)

    fun titleBar() = binding.titleBar

    fun hideTitleDivider() = binding.titleDivider.gone()

    abstract fun getBodyLayout(): Int

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