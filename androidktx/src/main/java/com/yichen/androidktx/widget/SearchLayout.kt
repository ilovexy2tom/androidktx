package com.yichen.androidktx.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.yichen.androidktx.R
import com.yichen.androidktx.core.*
import com.yichen.androidktx.databinding.KtxNumberEditLayoutBinding
import com.yichen.androidktx.databinding.KtxSearchLayoutBinding

/**
 * Description: 搜索框
 * Create by dance, at 2019/5/27
 */
open class SearchLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShapeLinearLayout(context, attributeSet, defStyleAttr) {
    private lateinit var binding: KtxSearchLayoutBinding
    var hint = ""
    var hintColor = Color.parseColor("#888888")
    var textColor = Color.parseColor("#222222")
    var textSize = 14.sp
    var clearIcon: Drawable? = null
    var searchIcon: Drawable? = null
    var clearIconSize = 20.dp
    var searchIconSize =  20.dp
    var showClearIconWhenEmpty = false  //内容为空时是否显示删除按钮
    var showSearchIcon = true  //是否显示搜索图标
    var searchIconPosition = 0  //搜索框默认在右边
    var searchIconSpace = 0  //搜索框和文字的间距

    init {
        binding = KtxSearchLayoutBinding.inflate(
            LayoutInflater.from(context),
            this
        )
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.SearchLayout)
        hint = ta.getString(R.styleable.SearchLayout_sl_hint) ?: ""
        hintColor = ta.getColor(R.styleable.SearchLayout_sl_hintColor, hintColor)
        textColor = ta.getColor(R.styleable.SearchLayout_sl_textColor, textColor)
        textSize = ta.getDimensionPixelSize(R.styleable.SearchLayout_sl_textSize, textSize)
        clearIcon = ta.getDrawable(R.styleable.SearchLayout_sl_clearIcon)
            ?: drawable(R.mipmap._ktx_ic_clear)
        searchIcon = ta.getDrawable(R.styleable.SearchLayout_sl_searchIcon)
            ?: drawable(R.mipmap._ktx_ic_search)
        clearIconSize = ta.getDimensionPixelSize(R.styleable.SearchLayout_sl_clearIconSize, clearIconSize)
        searchIconSize = ta.getDimensionPixelSize(R.styleable.SearchLayout_sl_searchIconSize, searchIconSize)
        showClearIconWhenEmpty = ta.getBoolean(R.styleable.SearchLayout_sl_showClearIconWhenEmpty, showClearIconWhenEmpty)
        showSearchIcon = ta.getBoolean(R.styleable.SearchLayout_sl_showSearchIcon, showSearchIcon)
        searchIconPosition = ta.getInt(R.styleable.SearchLayout_sl_searchIconPosition, searchIconPosition)
        searchIconSpace =
            ta.getDimensionPixelSize(R.styleable.SearchLayout_sl_searchIconSpace, searchIconSpace)
        ta.recycle()

        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        inflate(context, R.layout._ktx_search_layout, this)
        initSelf()
    }

    fun initSelf() {
        binding.ivClear.widthAndHeight(clearIconSize, clearIconSize)
        binding.ivClear.setImageDrawable(clearIcon)
        if(showSearchIcon){
            if(searchIconPosition==0){
                binding.ivSearchLeft.visible()
                binding.ivSearchLeft.widthAndHeight(searchIconSize, searchIconSize)
                binding.ivSearchRight.gone()
                binding.ivSearchLeft.setImageDrawable(searchIcon)
                binding.ivSearchLeft.margin(rightMargin = searchIconSpace)
            }else{
                binding.ivSearchRight.visible()
                binding.ivSearchRight.widthAndHeight(searchIconSize, searchIconSize)
                binding.ivSearchLeft.gone()
                binding.ivSearchRight.setImageDrawable(searchIcon)
                binding.ivSearchRight.margin(leftMargin = searchIconSpace)
            }
        }else{
            binding.ivSearchLeft.gone()
            binding.ivSearchRight.gone()
        }

        if(showClearIconWhenEmpty) {
            binding.ivClear.visible()
            //如果搜索图标也在右边，则强制隐藏
            if(searchIconPosition==1) binding.ivSearchRight.gone()
        } else {
            if(searchIconPosition==1) binding.ivClear.gone()
            else binding.ivClear.invisible()
        }

        binding.ivClear.click { binding.etContent.setText("") }
        binding.etContent.doAfterTextChanged {
            if (!it.isNullOrEmpty() || showClearIconWhenEmpty) {
                binding.ivClear.visible()
                if(searchIconPosition==1) binding.ivSearchRight.gone()
            } else {
                if(searchIconPosition==1) {
                    binding.ivSearchRight.visible()
                    binding.ivClear.gone()
                }else{
                    binding.ivClear.invisible()
                }
            }
        }

        binding.etContent.hint = hint
        binding.etContent.setHintTextColor(hintColor)
        binding.etContent.setTextColor(textColor)
        binding.etContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    }

    fun getEditText() = binding.etContent
    fun getClearView() = binding.ivClear

    fun setReadMode() {
        binding.ivClear.gone()
        binding.ivSearchLeft.gone()
        binding.ivSearchRight.gone()
        binding.etContent.isEnabled = false
    }
}