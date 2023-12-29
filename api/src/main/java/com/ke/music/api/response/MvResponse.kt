package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MvResponse(
    val id: Long,
    val name: String,
    val cover: String,
)
