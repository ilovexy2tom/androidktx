package com.yichen.androidktx.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.FragmentUtils
import com.yichen.androidktx.util.ViewBindingCreator

/**
 * Description: 外部容器要使用ViewPager2，自带懒加载效果
 * Create by dance, at 2019/5/16
 */
abstract class BindingFragment<VB: ViewBinding>: Fragment(), FragmentUtils.OnBackClickListener{

    var _binding:VB?=null
    val binding get()= _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(_binding==null)_binding = getLayoutBinding(inflater,container)
        return binding.root
    }




    private fun getLayoutBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB {
        return createViewBinding(inflater, container)!!
    }

    private fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB? {
        return ViewBindingCreator.createViewBindingFrag<VB>(javaClass, inflater, container)
    }


    override fun onBackClick() = false


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}