package com.demo.cl.movieguidekotlin.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.demo.cl.movieguidekotlin.api.PICTURE_BASE_URL

@BindingAdapter("image_url")
fun setImageURL(iv: ImageView, url: String) {
    Glide.with(iv).load(PICTURE_BASE_URL+url).into(iv)
}


@BindingAdapter("refresh")
fun setRefresh(sf:SwipeRefreshLayout,refreshListener: OnSwipeRefreshListener){
    sf.setOnRefreshListener {
        refreshListener.swipeToRefresh(sf)
        sf.isRefreshing=false
    }
}

interface OnSwipeRefreshListener{
     fun swipeToRefresh(view: View)
}
