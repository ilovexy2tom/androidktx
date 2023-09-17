package com.yichen.androidktx.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.setPadding
import androidx.core.widget.doAfterTextChanged
import com.yichen.androidktx.R
import com.yichen.androidktx.core.dp
import com.yichen.androidktx.core.margin
import com.yichen.androidktx.core.maxLength
import com.yichen.androidktx.core.sp
import com.yichen.androidktx.databinding.KtxNumberEditLayoutBinding

class NumberEditLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : ShapeLinearLayout (context, attributeSet, defStyleAttr){
    private lateinit var binding: KtxNumberEditLayoutBinding

    var mHint = ""
    var mHintColor = Color.parseColor("#888888")
    var mInputTextColor = Color.parseColor("#232323")
    var mInputTextSize = 14.sp
    var mInputBgColor = Color.TRANSPARENT
    var mInputCorner = 0
    var mInputPadding = 10.dp
    var mNumberTextColor = Color.parseColor("#777777")
    var mNumberTextSize = 12.sp
    var mMaxTextNumber = 100
    var mNumberTopSpace = 10.dp
    
    init {
        binding = KtxNumberEditLayoutBinding.inflate(
            LayoutInflater.from(context),
            this
        )

        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.NumberEditLayout)
        mHint = ta.getString(R.styleable.NumberEditLayout_nel_hint) ?: ""
        mHintColor = ta.getColor(R.styleable.NumberEditLayout_nel_hintColor, mHintColor)
        mInputTextColor = ta.getColor(R.styleable.NumberEditLayout_nel_inputTextColor, mInputTextColor)
        mInputTextSize = ta.getDimensionPixelSize(R.styleable.NumberEditLayout_nel_inputTextSize, mInputTextSize)
        mInputBgColor = ta.getColor(R.styleable.NumberEditLayout_nel_inputBgColor, mInputBgColor)
        mInputCorner = ta.getDimensionPixelSize(R.styleable.NumberEditLayout_nel_inputCorner, mInputCorner)
        mInputPadding = ta.getDimensionPixelSize(R.styleable.NumberEditLayout_nel_inputPadding, mInputPadding)
        mNumberTextColor = ta.getColor(R.styleable.NumberEditLayout_nel_numberTextColor, mNumberTextColor)
        mNumberTextSize = ta.getDimensionPixelSize(R.styleable.NumberEditLayout_nel_numberTextSize, mNumberTextSize)
        mMaxTextNumber = ta.getInt(R.styleable.NumberEditLayout_nel_maxTextNumber, mMaxTextNumber)
        mNumberTopSpace = ta.getDimensionPixelSize(R.styleable.NumberEditLayout_nel_numberTopSpace, mNumberTopSpace)

        orientation = LinearLayout.VERTICAL
        inflate(context, R.layout._ktx_number_edit_layout, this)
        
        applyAttr()
    }

    fun getEditText() = binding.etContent
    fun getNumberView() = binding.tvNum

    fun setupSelf(hint: String? = null, hintColor: Int? = null, inputTextColor: Int? = null,
        inputTextSize: Float? = null, inputBgColor: Int? = null, inputCorner: Int? = null, inputPadding: Int? = null,
        numberTextColor: Int? = null, numberTextSize: Float? = null, maxTextNumber: Int? = null,
        numberTopSpace: Int? = null){
        if(hint!=null) mHint = hint
        if(hintColor!=null) mHintColor = hintColor
        if(inputTextColor!=null) mInputTextColor = inputTextColor
        if(inputTextSize!=null) mInputTextSize = inputTextSize.toInt()
        if(inputBgColor!=null) mInputBgColor = inputBgColor
        if(inputCorner!=null) mInputCorner = inputCorner
        if(inputPadding!=null) mInputPadding = inputPadding
        if(numberTextColor!=null) mNumberTextColor = numberTextColor
        if(numberTextSize!=null) mNumberTextSize = numberTextSize.toInt()
        if(maxTextNumber!=null) mMaxTextNumber = maxTextNumber
        if(numberTopSpace!=null) mNumberTopSpace = numberTopSpace
        applyAttr()
    }
    
    private fun applyAttr() {
        binding.etContent.hint = mHint
        binding.etContent.setHintTextColor(mHintColor)
        binding.etContent.setTextColor(mInputTextColor)
        binding.etContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, mInputTextSize.toFloat())
        binding.etContent.setup(solid = mInputBgColor, corner = mInputCorner)
        binding.etContent.maxLength(mMaxTextNumber)
        binding.etContent.setPadding(mInputPadding)
        binding.tvNum.setTextColor(mNumberTextColor)
        binding.tvNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNumberTextSize.toFloat())
        binding.tvNum.margin(topMargin = mNumberTopSpace)

        val currentLength = binding.etContent.text?.length ?:0
        binding.tvNum.text = "${currentLength}/${mMaxTextNumber}"
        binding.etContent.doAfterTextChanged {
            val currentLength = binding.etContent.text?.length ?:0
            binding.tvNum.text = "${currentLength}/${mMaxTextNumber}"
        }
    }
}