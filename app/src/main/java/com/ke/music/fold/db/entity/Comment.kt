package com.ke.music.fold.db.entity

import androidx.room.Entity

@Entity(tableName = "comment", primaryKeys = ["commentId", "parentCommentId"])
data class Comment(
    val commentId: Long,
    val sourceId: Long,
    val sourceType: Int,
    val username: String,
    val userId: Long,
    val userAvatar: String,
    val content: String?,
    val timeString: String,
    val likedCount: Int,
    val ipLocation: String,
    val owner: Boolean,
    val liked: Boolean,
    val parentCommentId: Long,
    /**
     * 子评论数
     */
    val replyCount: Int,

    )
