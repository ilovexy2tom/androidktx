package com.yichen.androidktx.base

import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.LanguageUtils


/**
 * Description: 适用于ViewPager中的懒加载Fragment
 * Create by dance, at 2019/4/22
 */
abstract class PagerLazyFragment<VB:ViewBinding> : BindingFragment<VB>() {
    protected var isInit = false

    override fun onResume() {
        super.onResume()
        lazyInit()
    }

    private fun lazyInit() {
        if (_binding!=null && userVisibleHint && !isInit ) {
            initView()
            initData()
            LanguageUtils.attachBaseContext(requireContext())
            isInit = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        lazyInit()
        if(isInit){
            if(isVisibleToUser) onShow() else onHide()
        }
    }

    //执行初始化，只会执行一次
    abstract fun initView()
    abstract fun initData()
    open fun onShow(){}
    open fun onHide(){}

    override fun onBackClick() = false
}