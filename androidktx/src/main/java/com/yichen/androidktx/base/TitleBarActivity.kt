package com.yichen.androidktx.base

import android.view.View
import android.widget.FrameLayout
import com.yichen.androidktx.R
import com.yichen.androidktx.core.click
import com.yichen.androidktx.core.gone
import com.yichen.androidktx.core.visible
import com.yichen.androidktx.widget.TitleBar


abstract class TitleBarActivity : AdaptActivity(){
    override fun getLayoutId(): Int {
        return R.layout._ktx_activity_titlebar
    }

    override fun initView() {
        findViewById<FrameLayout>(R.id.flBody).addView(View.inflate(this, getBodyLayout(), null))
        findViewById<TitleBar>(R.id.titleBar).leftImageView().click { finish() }
    }

    fun setStatusBarColor(color: Int) = findViewById<View>(R.id.fakeTitleBar).setBackgroundColor(color)

    fun titleBar() = findViewById<TitleBar>(R.id.titleBar)

    fun hideTitleDivider() = findViewById<View>(R.id.titleDivider).gone()

    abstract fun getBodyLayout(): Int

    fun hideTitleBar(){
        titleBar().gone()
        findViewById<View>(R.id.fakeTitleBar).gone()
        findViewById<View>(R.id.titleDivider).gone()
    }
    fun hideTitleBarWithMarginTop(){
        titleBar().gone()
        findViewById<View>(R.id.titleDivider).gone()
    }

    fun showTitleBar(){
        titleBar().visible()
        findViewById<View>(R.id.fakeTitleBar).visible()
        findViewById<View>(R.id.titleDivider).visible()
    }
}