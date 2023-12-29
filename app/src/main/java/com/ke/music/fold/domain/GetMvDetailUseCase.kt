package com.ke.music.fold.domain

import com.ke.music.api.MusicApi
import com.ke.music.api.response.BaseResponse
import com.ke.music.api.response.MvDetailResponse
import javax.inject.Inject

internal class GetMvDetailUseCase @Inject constructor(private val musicApi: MusicApi) :UseCase<Long,BaseResponse<MvDetailResponse>>() {

    override suspend fun execute(parameters: Long): BaseResponse<MvDetailResponse> {
        return musicApi.getMvDetail(parameters)
    }
}