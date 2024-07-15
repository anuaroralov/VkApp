package com.example.vkapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Link(
    val url: String,
    val caption: String?,
    val photo: String?,
    val title: String?
) : Parcelable