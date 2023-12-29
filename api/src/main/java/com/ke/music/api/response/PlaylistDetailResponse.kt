package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaylistDetailResponse(
    val playlist: PlaylistResponse,
    val songs: List<SongResponse>,
    val dynamic: PlaylistDynamicResponse,
)
