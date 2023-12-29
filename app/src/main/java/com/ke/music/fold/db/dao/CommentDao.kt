package com.ke.music.fold.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ke.music.fold.db.entity.Comment

@Dao
interface CommentDao {

    /**
     * 根据资源id和资源类型删除评论
     */
    @Query("delete from comment where sourceId = :sourceId and sourceType = :sourceType")
    suspend fun deleteBySourceAndType(sourceId: Long, sourceType: Int)


    @Query("delete from comment where sourceId = :sourceId and sourceType = :sourceType and parentCommentId = :parentCommentId")
    suspend fun deleteBySourceAndTypeAndCommentId(
        sourceId: Long,
        sourceType: Int,
        parentCommentId: Long,
    )

    @Insert
    suspend fun insertAll(comments: List<Comment>)

    @Query("select * from comment where sourceId = :sourceId and sourceType = :sourceType")
    fun getComments(sourceId: Long, sourceType: Int): PagingSource<Int, Comment>


    @Query("select * from comment where sourceId = :sourceId and sourceType = :sourceType and parentCommentId = :parentCommentId and parentCommentId != 0")
    fun getComments(
        sourceId: Long,
        sourceType: Int,
        parentCommentId: Long,
    ): PagingSource<Int, Comment>
}