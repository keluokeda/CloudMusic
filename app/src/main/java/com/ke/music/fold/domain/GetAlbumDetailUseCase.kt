package com.ke.music.fold.domain

import com.ke.music.api.MusicApi
import com.ke.music.api.response.AlbumDetailResponse
import com.ke.music.api.response.BaseResponse
import javax.inject.Inject

internal class GetAlbumDetailUseCase @Inject constructor(private val musicApi: MusicApi) :
    UseCase<Long, BaseResponse<AlbumDetailResponse>>() {

    override suspend fun execute(parameters: Long): BaseResponse<AlbumDetailResponse> {
        return musicApi.getAlbumDetail(parameters)
    }
}