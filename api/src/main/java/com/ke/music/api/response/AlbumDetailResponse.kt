package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumDetailResponse(
    val isSub: Boolean,
    val songs: List<SongResponse>,
    val name: String,
    val description: String?,
    val id: Long,
    val picUrl: String,
    val artists: List<ArtistResponse>,
    val image: String,

    )
