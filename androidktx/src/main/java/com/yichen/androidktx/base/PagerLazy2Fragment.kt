package com.yichen.androidktx.base

import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.LogUtils


/**
 * Description: 适用于ViewPager2中的懒加载Fragment
 * Create by dance, at 2023/4/11
 */
abstract class PagerLazy2Fragment : Fragment(), FragmentUtils.OnBackClickListener {
    protected var cacheView: View? = null
    protected var isInit = false

    companion object {
        const val TAG = "PagerLazy2Fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (cacheView == null) cacheView = inflater.inflate(getLayoutId(), container, false)
        return cacheView!!
    }

    override fun onResume() {
        super.onResume()
        LogUtils.dTag(TAG, "name = $this${javaClass.simpleName}  onResume ")
        lazyInit()
    }

    private fun lazyInit() {
        if (cacheView != null && userVisibleHint && !isInit) {
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