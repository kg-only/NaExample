package com.example.naexample.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Photos(

    var id: Int? = null,
    var width: Int? = null,
    var height: Int? = null,
    var url: String? = null,
    var photographer: String? = null,
    @field:Json(name = "photographer_url") var photographerUrl: String? = null,
    var avgColor: String? = null,
    var src: Src? = Src(),
    var liked: Boolean? = null,
    var alt: String? = null
):Parcelable