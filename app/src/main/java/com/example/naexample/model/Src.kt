package com.example.naexample.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Src(

    var original: String? = null,
    var large2x: String? = null,
    var large: String? = null,
    var medium: String? = null,
    var small: String? = null,
    var portrait: String? = null,
    var landscape: String? = null,
    var tiny: String? = null

):Parcelable