package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KeyResponse(
    val key: String,
    val url: String,
)
