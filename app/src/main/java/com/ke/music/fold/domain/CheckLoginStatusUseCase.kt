package com.ke.music.fold.domain

import com.ke.music.api.MusicApi
import com.ke.music.api.response.BaseResponse
import javax.inject.Inject

internal class CheckLoginStatusUseCase @Inject constructor(private val musicApi: MusicApi) :
    UseCase<Unit, BaseResponse<Int>>() {

    override suspend fun execute(parameters: Unit): BaseResponse<Int> {
        return musicApi.loginStatus()
    }
}