package com.ke.music.fold.domain

import com.ke.music.api.MusicApi
import com.ke.music.api.response.BaseResponse
import com.ke.music.api.response.PlaylistDetailResponse
import javax.inject.Inject

internal class GetPlaylistDetailUseCase @Inject constructor(private val musicApi: MusicApi) :
    UseCase<Long, BaseResponse<PlaylistDetailResponse>>() {

    override suspend fun execute(parameters: Long): BaseResponse<PlaylistDetailResponse> {
        return musicApi.getPlaylistDetail(parameters)
    }
}