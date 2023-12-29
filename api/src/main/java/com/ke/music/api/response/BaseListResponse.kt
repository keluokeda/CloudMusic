package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseListResponse<T>(
    val code: Int = 200,
    val success: Boolean = true,
    val message: String = "success",
    val data: List<T>,
    val hasMore: Boolean = false,
    val cursor: Long = 0,
)
