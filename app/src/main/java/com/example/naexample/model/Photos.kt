package com.example.naexample.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

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
)