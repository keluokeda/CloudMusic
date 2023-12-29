package com.ke.music.fold.ui.playlist_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.api.response.BaseResponse
import com.ke.music.api.response.PlaylistDetailResponse
import com.ke.music.fold.domain.GetPlaylistDetailUseCase
import com.ke.music.fold.entity.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PlaylistDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPlaylistDetailUseCase: GetPlaylistDetailUseCase,
) :
    ViewModel() {

    private val _uiState = MutableStateFlow<PlaylistDetailUiState>(PlaylistDetailUiState.Loading)

    internal val uiState: StateFlow<PlaylistDetailUiState> = _uiState

    private val playlistId = savedStateHandle.get<Long>("id")!!

    init {
        refresh()
    }

    internal fun refresh() {
        viewModelScope.launch {
            _uiState.value = PlaylistDetailUiState.Loading
            val response =
                getPlaylistDetailUseCase(playlistId).successOr(BaseResponse.defaultError())

            if (response.success) {
                _uiState.value = PlaylistDetailUiState.Success(response.data!!)
            } else {
                _uiState.value = PlaylistDetailUiState.Error(response.message)
            }
        }
    }
}

internal sealed interface PlaylistDetailUiState {
    data class Success(val content: PlaylistDetailResponse) : PlaylistDetailUiState
    data class Error(val message: String) : PlaylistDetailUiState
    data object Loading : PlaylistDetailUiState
}