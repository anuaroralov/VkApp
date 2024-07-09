package com.example.vkapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val description: String,
    val duration: Int,
    val thumbnailUrl: String?,
    val views: Int,
    val comments: Int
): Parcelable