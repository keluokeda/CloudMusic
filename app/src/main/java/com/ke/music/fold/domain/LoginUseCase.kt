package com.ke.music.fold.domain

import com.ke.music.api.MusicApi
import com.ke.music.api.response.BaseResponse
import com.ke.music.api.response.LoginResponse
import com.ke.music.fold.store.TokenStore
import javax.inject.Inject

/**
 * 登录成功的时候会自动保存token
 */
internal class LoginUseCase @Inject constructor(
    private val musicApi: MusicApi,
    private val tokenStore: TokenStore,
) :
    UseCase<String, BaseResponse<LoginResponse>>() {


    override suspend fun execute(parameters: String): BaseResponse<LoginResponse> {
        val result = musicApi.login(parameters)

        if (result.success) {
            tokenStore.token = result.data!!.token
        }

        return result
    }
}