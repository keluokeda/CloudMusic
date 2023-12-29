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
class CommentRemoteMediator internal constructor(
    private val sourceType: Int,
    private val sourceId: Long,
    private val commentDao: CommentDao,
    private val musicApi: MusicApi,
) :
    RemoteMediator<Int, Comment>() {

    private var index = 1

    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Comment>,
    ): MediatorResult {

        Logger.d("开始加载数据 $loadType $index")

        try {
            when (loadType) {
                LoadType.REFRESH -> {
                    index = 1
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    index += 1
                }
            }


            val response = musicApi.getComments(
                sourceType,
                sourceId,
                index,
                50
            )

            if (index == 1) {
                commentDao.deleteBySourceAndType(sourceId, sourceType)
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
                    0L,
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