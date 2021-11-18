package com.example.marvelproject.domain.model

import android.os.Parcelable
import com.example.marvelproject.model.Thumbnail
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(

    val description: String,
    val id: Int,
    val name: String,
    val thumbnail: Thumbnail,

    ) : Parcelable