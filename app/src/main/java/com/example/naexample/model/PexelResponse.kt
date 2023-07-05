package com.example.naexample.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class PexelResponse(
    var photos: List<Photos> = arrayListOf(),
)