package com.udacity.asteroidradar

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageOfToday(val url: String,
                        val title: String): Parcelable