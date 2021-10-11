package com.example.mastermusic.model.utilities

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.NumberFormat

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String) {
    Glide.with(view.context)
        .load(imageUrl)
        .apply(RequestOptions.circleCropTransform())
        .into(view)
}

@BindingAdapter("price")
fun itemPrice(view: TextView, value: Double){
    val formatter = NumberFormat.getCurrencyInstance()
    val text = "${formatter.format(value)} / each"
    view.text = text
}