package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SongResponse(
    val id: Long,
    val name: String,
    val album: AlbumResponse,
    val artists: List<ArtistResponse>,
    /**
     * mv的id，如果是0表示没有
     */
    val mv: Long,
) {
    val subTitle: String
        get() = "${artists.joinToString("/") { it.name }} - ${album.name}"
}


@JsonClass(generateAdapter = true)
data class ArtistResponse(
    val id: Long,
    val name: String,
    val avatar: String? = null,
    val followed: Boolean = false,
)


@JsonClass(generateAdapter = true)
data class AlbumResponse(
    val id: Long,
    val name: String,

    val imageUrl: String,
)