package com.yichen.androidktx.okhttp

import java.io.IOException

/**
 * Description:
 * Create by yichen, at 2018/12/28
 */

interface HttpCallback<T>{
    fun onSuccess(t: T)
    fun onFail(e: IOException){}
}
