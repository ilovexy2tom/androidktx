package com.yichen.androidktx.baseVB

import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.LogUtils


/**
 * Description: 适用于ViewPager2中的懒加载Fragment
 * Create by dance, at 2023/4/11
 */
abstract class PagerLazy2VBFragment <VB: ViewBinding> : BindingFragment<VB>(), FragmentUtils.OnBackClickListener {
    protected var isInit = false

    companion object {
        const val TAG = "PagerLazy2Fragment"
    }


    override fun onResume() {
        super.onResume()
        LogUtils.dTag(TAG, "name = $this${javaClass.simpleName}  onResume ")
        lazyInit()
    }

    private fun lazyInit() {
        if (_binding != null && userVisibleHint && !isInit) {
            LogUtils.dTag(TAG, "lazyInit")
            initView()
            initData()
            isInit = true
        }else{
            onShow()
        }
    }




    override fun onPause() {
        super.onPause()
        onHide()
        LogUtils.dTag(TAG, "name = $this${javaClass.simpleName}  onPause ")
    }

    //执行初始化，只会执行一次
    protected abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract fun initData()
    open fun onShow() {}
    open fun onHide() {}

    override fun onBackClick() = false

}