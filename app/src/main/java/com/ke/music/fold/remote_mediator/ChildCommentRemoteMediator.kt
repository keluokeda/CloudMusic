package com.ke.music.fold.remote_mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ke.music.api.MusicApi
import com.ke.music.fold.db.dao.CommentDao
import com.ke.music.fold.db.entity.Comment
import com.orhanobut.logger.Logger

@OptIn(ExperimentalPagingApi::class)
class ChildCommentRemoteMediator internal constructor(
    private val sourceType: Int,
    private val sourceId: Long,
    private val commentDao: CommentDao,
    private val musicApi: MusicApi,
    var commentId: Long,
) :
    RemoteMediator<Int, Comment>() {

    private var time = 0L

    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Comment>,
    ): MediatorResult {

        Logger.d("加载子评论数据 loadType = $loadType, commentId = $commentId")

        if (commentId == 0L) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        try {
            when (loadType) {
                LoadType.REFRESH -> {
                    time = 1
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
//                    index += 1
                }
            }


            val response = musicApi.getChildComments(
                commentId,
                sourceType,
                sourceId,
                50,
                time
            )

            if (time == 0L) {
                commentDao.deleteBySourceAndTypeAndCommentId(sourceId, sourceType, commentId)
            }


            commentDao.insertAll(response.data.map {
                Comment(
                    it.commentId,
                    sourceId,
                    sourceType,
                    it.user.nickname,
                    it.user.userId,
                    it.user.avatarUrl,
                    it.content,
                    it.timeString,
                    it.likedCount,
                    it.ipLocation,
                    it.owner,
                    it.liked,
                    commentId,
                    it.replyCount
                )
            })

//            commentRepository.saveComments(
//                response.comments ?: emptyList(),
//                commentType,
//                sourceId,
//                deleteOld = index == 1
//            )


            return MediatorResult.Success(
                endOfPaginationReached = !response.hasMore
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)
        }


    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
}