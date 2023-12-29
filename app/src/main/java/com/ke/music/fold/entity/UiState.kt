package com.ke.music.fold.entity

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data object Error : UiState<Nothing>
    data class Success<R>(val content: R) : UiState<R>
}