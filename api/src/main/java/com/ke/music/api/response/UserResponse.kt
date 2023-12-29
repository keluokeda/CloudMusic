package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    val userId: Long,
    val signature: String,
    val nickname: String,
    val avatarUrl: String,
    val followed: Boolean,
)
