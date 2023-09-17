package com.yichen.androidktx.core

import android.graphics.Rect
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yichen.androidktx.R
import com.yichen.androidktx.widget.ShapeImageView
import com.youth.banner.adapter.BannerAdapter

class CommonBannerAdapter(
    data: List<Any>, var margin: Rect = Rect(), var cornerRadius: Int = 0,
    var placeHolder: Int,
    var pageElevation: Int = 0, var onItemClick: ((Int) -> Unit)? = null
) :
    BannerAdapter<Any, CommonBannerAdapter.BannerViewHolder>(data) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout._ktx_adapter_common_banner, parent, false)
        return BannerViewHolder(item)
    }

    override fun onBindView(holder: BannerViewHolder, t: Any, position: Int, size: Int) {
        holder.itemView.margin(margin.left, margin.top, margin.right, margin.bottom)
        (holder.shapeImageView).apply {
            Glide.with(this)
                .load(t)
                .placeholder(placeHolder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this)
//            load(t,isCrossFade = false)
            setup(corner = cornerRadius)
            elevation = pageElevation.toFloat()
            click { onItemClick?.invoke(position) }
        }
    }

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var shapeImageView = itemView.findViewById<ShapeImageView>(R.id.bannerView)
    }
}

