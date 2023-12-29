package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MvDetailResponse(
    val id: Long,
    val name: String,
    val artists: List<ArtistResponse>,
    val url: String,
    val cover: String,
    val playCount: Int,
    val subCount: Int,
    val shareCount: Int,
    val commentCount: Int,
    val simiMvs: List<MvResponse>,
)
