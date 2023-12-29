package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentResponse(
    val commentId: Long = 0,
    val user: UserResponse,
    val content: String?,
    val timeString: String = "",
    val time: Long = 0,
    val likedCount: Int = 0,
    val ipLocation: String,
    val owner: Boolean = false,
    val liked: Boolean = false,
    val parentCommentId: Long,
    /**
     * 子评论数
     */
    val replyCount: Int = 0,
)


