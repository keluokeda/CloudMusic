package com.ke.music.fold.ui.comment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.ke.music.api.MusicApi
import com.ke.music.fold.db.dao.CommentDao
import com.ke.music.fold.db.entity.Comment
import com.ke.music.fold.remote_mediator.ChildCommentRemoteMediator
import com.ke.music.fold.remote_mediator.CommentRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
internal class CommentViewModel @Inject constructor(
    private val commentDao: CommentDao,
    savedStateHandle: SavedStateHandle,
    commentRemoteMediator: CommentRemoteMediator,
    private val musicApi: MusicApi,
) : ViewModel() {


    private val sourceId = savedStateHandle.get<Long>("id")!!
    private val sourceType = savedStateHandle.get<Int>("type")!!

    @OptIn(ExperimentalPagingApi::class)
    val comments: Flow<PagingData<Comment>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            initialLoadSize = 50
        ),
        remoteMediator = commentRemoteMediator
    ) {
        commentDao.getComments(sourceId, sourceType)
    }.flow
        .cachedIn(viewModelScope)

    private val _selectedCommentId = MutableStateFlow(0L)

    private val childCommentRemoteMediator = ChildCommentRemoteMediator(
        sourceType,
        sourceId,
        commentDao,
        musicApi,
        _selectedCommentId.value
    )


    /**
     * 点击了查看更多评论
     */
    internal fun onMoreCommentClick(commentId: Long) {
        _selectedCommentId.value = commentId
        childCommentRemoteMediator.commentId = commentId
    }


    @OptIn(ExperimentalPagingApi::class)
    internal val childComments = Pager(
        config = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            initialLoadSize = 50
        ),
        pagingSourceFactory = {
            commentDao.getComments(sourceId, sourceType, _selectedCommentId.value)
        },
        remoteMediator = childCommentRemoteMediator
    ).flow.cachedIn(viewModelScope)


}