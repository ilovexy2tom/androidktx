package com.yichen.androidktx.util

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 * @author by DELL
 * @date on 2020/06/11
 * @describe
 */
object ViewBindingCreator {
    /**
     * 通过实例工厂去实例化相应类
     *
     * @param <T> 返回实例的泛型类型
     * @return
    </T> */

    open fun <VB : ViewBinding?> createViewBinding(
        targetClass: Class<*>,
        layoutInflater: LayoutInflater?
    ): VB? {
        val type: Type = targetClass.genericSuperclass as Type
        if (type is ParameterizedType) {
            try {
                val types: Array<Type> = (type as ParameterizedType).actualTypeArguments
                for (type1 in types) {
                    if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            type1.typeName
                                    .endsWith("Binding")
                        } else {
                            (type1 as Class<*>).simpleName.endsWith("Binding")
                        }
                    ) {
                        val method = (type1 as Class<VB>).getMethod(
                            "inflate",
                            LayoutInflater::class.java
                        )
                        return method.invoke(null, layoutInflater) as VB
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }


    open fun <VB : ViewBinding?> createViewBindingFrag(
        targetClass: Class<*>,
        layoutInflater: LayoutInflater?,
        container: ViewGroup?
    ): VB? {
        val type: Type = targetClass.genericSuperclass as Type
        if (type is ParameterizedType) {
            try {
                val types: Array<Type> = (type as ParameterizedType).actualTypeArguments
                for (type1 in types) {
                    if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            type1.typeName
                                .endsWith("Binding")
                        } else {
                            (type1 as Class<*>).simpleName.endsWith("Binding")
                        }) {
                        val method =
                            (type1 as Class<VB>).getMethod(
                                "inflate",
                                LayoutInflater::class.java,
                                ViewGroup::class.java,
                                Boolean::class.java
                            )
                        return method.invoke(null, layoutInflater, container, false) as VB
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }
}