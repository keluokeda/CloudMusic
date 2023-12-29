package com.ke.music.fold.domain

import com.ke.music.api.MusicApi
import com.ke.music.api.response.BaseResponse
import com.ke.music.api.response.KeyResponse
import javax.inject.Inject

internal class CreateLoginKeyUseCase @Inject constructor(private val musicApi: MusicApi) :
    UseCase<Unit, BaseResponse<KeyResponse>>() {
    override suspend fun execute(parameters: Unit): BaseResponse<KeyResponse> {
        return musicApi.key()
    }
}