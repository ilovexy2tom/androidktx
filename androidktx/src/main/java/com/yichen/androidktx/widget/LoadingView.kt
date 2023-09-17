package com.yichen.androidktx.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.yichen.androidktx.R
import com.yichen.androidktx.databinding.LoadingBinding


/**
 * @author by Dell
 * @date on 2022/12/4
 * @describe
 */
class LoadingView : FrameLayout {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    private lateinit var binding: LoadingBinding
    init {
        binding = LoadingBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        )
//        inflate(context, R.layout._loading, this)
        if (!isInEditMode) {
            post {
                Glide.with(this)
                    .asGif()
                    .load(R.mipmap.loading)
                    .into(binding.ivLoading)
            }
        }
    }
}