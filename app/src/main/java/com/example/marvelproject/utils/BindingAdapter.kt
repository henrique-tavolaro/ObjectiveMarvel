package com.example.marvelproject.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.marvelproject.R
import com.example.marvelproject.domain.model.Result

@BindingAdapter("set_character")
fun TextView.setCharacter(item: Result){
    item?.let {
        text = item.name
    }
}

@BindingAdapter("loadImage")
fun bindingImage(imageView: ImageView, item: Result){
    item?.let {
        val image = "${item.thumbnail.path}.${item.thumbnail.extension}"
        Glide
            .with(imageView.context)
            .load(image)
            .circleCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(imageView)
    }
}