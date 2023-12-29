package com.ke.music.fold.domain

import com.ke.music.api.MusicApi
import com.ke.music.api.response.ArtistDetailResponse
import com.ke.music.api.response.BaseResponse
import javax.inject.Inject

internal class GetArtistDetailUseCase @Inject constructor(private val musicApi: MusicApi) :
    UseCase<Long, BaseResponse<ArtistDetailResponse>>() {
    override suspend fun execute(parameters: Long) =
        musicApi.getArtistDetail(parameters)

}