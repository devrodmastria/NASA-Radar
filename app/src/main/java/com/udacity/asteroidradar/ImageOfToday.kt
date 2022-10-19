package com.udacity.asteroidradar

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageOfToday(val imageURL: String,
                        val imageDescription: String): Parcelable {}