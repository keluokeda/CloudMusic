package com.ke.music.fold.di

import androidx.lifecycle.SavedStateHandle
import com.ke.music.api.MusicApi
import com.ke.music.fold.db.dao.CommentDao
import com.ke.music.fold.remote_mediator.CommentRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideCommentRemoteMediator(
        musicApi: MusicApi,
        commentDao: CommentDao,
        savedStateHandle: SavedStateHandle,
    ): CommentRemoteMediator {
        return CommentRemoteMediator(
            savedStateHandle.get<Int>("type")!!,
            savedStateHandle.get<Long>("id")!!,
            commentDao,
            musicApi
        )
    }
}