package com.example.vkapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    val title: String,
    val description: String,
    val thumbnailUrl: String?,
    val views: Int,
    val comments: Int,
    val videoUrl: String?
) : Parcelable