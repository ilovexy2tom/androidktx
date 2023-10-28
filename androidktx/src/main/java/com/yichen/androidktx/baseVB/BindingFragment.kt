package com.yichen.androidktx.baseVB

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.FragmentUtils
import com.yichen.androidktx.util.ViewBindingCreator

/**
 * Description:
 * 之所以[_binding]没有用private修饰，是因为 [_binding] 某些情况下，fragment会被销毁，
 * 但仍然需要使用[_binding]里面的view， 判断非空使用[_binding]?.let{...}
 * Create by dance, at 2023/10/28
 */
abstract class BindingFragment<VB : ViewBinding> : Fragment(), FragmentUtils.OnBackClickListener {

    var _binding: VB? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) _binding = getLayoutBinding(inflater, container)
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