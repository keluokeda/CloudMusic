package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponse<T>(
    val code: Int,
    val success: Boolean,
    val message: String,
    val data: T?,
) {
    companion object {
        fun <T> success(data: T?): BaseResponse<T> {
            return BaseResponse(200, true, "success", data)
        }

        fun <T> error(code: Int, message: String): BaseResponse<T> {
            return BaseResponse(code, false, message, null)
        }

        fun <T> defaultError(): BaseResponse<T> {
            return BaseResponse(500, false, "网络出错了", null)
        }

    }
}