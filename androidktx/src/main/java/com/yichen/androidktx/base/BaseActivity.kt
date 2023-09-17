package com.yichen.androidktx.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LanguageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.yichen.androidktx.util.ViewBindingCreator

/**
 * Description:
 * Create by dance, at 2019/5/16
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected val binding by lazy {
        ViewBindingCreator.createViewBinding<VB>(
            mClass,
            layoutInflater
        )!!
    }

    open val mClass:Class<*> by lazy { javaClass }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initData()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        LanguageUtils.attachBaseContext(newBase)
    }


    protected abstract fun initView()
    protected abstract fun initData()

    override fun onPause() {
        super.onPause()
        KeyboardUtils.hideSoftInput(this)
    }

    private var lastBackPressTime = 0L

    /**
     * 两次返回退出Activity
     */
    protected fun doubleBackToFinish(duration: Long = 2000, toast: String = "再按一次退出") {
        if (!FragmentUtils.dispatchBackPress(supportFragmentManager)) {
            if (System.currentTimeMillis() - lastBackPressTime < duration) {
                ToastUtils.cancel()
                super.onBackPressed()
            } else {
                lastBackPressTime = System.currentTimeMillis()
                ToastUtils.showShort(toast)
            }
        }
    }

    override fun onBackPressed() {
        if (!FragmentUtils.dispatchBackPress(supportFragmentManager)) {
            super.onBackPressed()
        }
    }
}