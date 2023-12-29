package com.ke.music.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistDetailResponse(
    val hotSongs: List<SongResponse>,
    val artist: ArtistResponse,
    val desc: ArtistDescResponse,
    val simiArtists: List<ArtistResponse>,
    val mvs: List<MvResponse>,
    val hotAlbums: List<AlbumResponse>,
)


@JsonClass(generateAdapter = true)
data class ArtistDescResponse(
    val briefDesc: String,
    val introduction: List<ArtistIntroduce>,
)

@JsonClass(generateAdapter = true)
data class ArtistIntroduce(
    val ti: String,
    val txt: String,
)