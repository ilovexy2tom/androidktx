package com.yichen.androidktx.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.FragmentUtils
import com.yichen.androidktx.core.postDelay
import com.yichen.statelayout.StateLayout

/**
 * 自带StateLayout的Fragment基类，适用于ViewPager的懒加载方案
 */
abstract class PagerStateFragment<VB:ViewBinding> : BindingFragment<VB>() {
    protected var isInit = false
    protected var stateLayout: StateLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

            stateLayout = StateLayout(requireContext()).wrap(_binding?.root)
            onConfigStateLayout()
            stateLayout!!.showLoading()
        return stateLayout!!
    }

    /**
     * 用来对StateLayout进行各种配置
     */
    open fun onConfigStateLayout(){

    }

    open fun showContent() = stateLayout?.showContent()
    open fun showLoading() = stateLayout?.showLoading()
    open fun showError() = stateLayout?.showError()
    open fun showEmpty(){
        stateLayout?.showEmpty()
    }

    //是否自动显示Content
    open fun autoShowContent() = false

    override fun onResume() {
        super.onResume()
        lazyInit()
    }

    private fun lazyInit() {
        if (_binding != null && userVisibleHint && !isInit) {
            initView()
            initData()
            if(autoShowContent())postDelay(350){showContent()}
            isInit = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        lazyInit()
        if(isVisibleToUser) onShow() else onHide()
    }

    //执行初始化，只会执行一次
    protected abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract fun initData()
    open fun onShow(){}
    open fun onHide(){}

    override fun onBackClick() = false

}