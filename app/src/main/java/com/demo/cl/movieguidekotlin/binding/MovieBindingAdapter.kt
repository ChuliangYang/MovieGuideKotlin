package com.demo.cl.movieguidekotlin.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.demo.cl.movieguidekotlin.api.PICTURE_BASE_URL

@BindingAdapter("image_url")
fun setImageURL(iv: ImageView, url: String) {
    Glide.with(iv).load(PICTURE_BASE_URL+url).into(iv)
}



