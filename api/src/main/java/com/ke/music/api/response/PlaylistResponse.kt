package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaylistResponse(
    val id: Long,
    val creator: UserResponse,
    val coverImgUrl: String,
    val name: String,
    val tags: List<String>,
    val description: String?,
    val trackCount: Int,
    val playCount: Int,
)
