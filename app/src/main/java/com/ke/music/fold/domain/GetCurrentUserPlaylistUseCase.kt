package com.ke.music.fold.domain

import com.ke.music.api.MusicApi
import com.ke.music.api.response.PlaylistResponse
import javax.inject.Inject

internal class GetCurrentUserPlaylistUseCase @Inject constructor(private val musicApi: MusicApi) :
    UseCase<Unit, List<PlaylistResponse>>() {

    override suspend fun execute(parameters: Unit): List<PlaylistResponse> {
        return musicApi.getCurrentUserPlaylists().data!!
    }
}