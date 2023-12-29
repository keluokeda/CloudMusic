package com.ke.music.fold.ui.user_playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.api.MusicApi
import com.ke.music.api.response.PlaylistResponse
import com.ke.music.fold.domain.GetCurrentUserPlaylistUseCase
import com.ke.music.fold.entity.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class UserPlaylistsViewModel @Inject constructor(private val getCurrentUserPlaylistUseCase: GetCurrentUserPlaylistUseCase) :
    ViewModel() {

    private val _uiState =
        MutableStateFlow<CurrentUserPlaylistUiState>(CurrentUserPlaylistUiState.Loading)

    val uiState: StateFlow<CurrentUserPlaylistUiState>
        get() = _uiState


    init {
        refresh()
    }

    internal fun refresh() {
        viewModelScope.launch {
            _uiState.value = CurrentUserPlaylistUiState.Success(
                getCurrentUserPlaylistUseCase(Unit).successOr(
                    emptyList()
                )
            )

        }

    }
}

sealed interface CurrentUserPlaylistUiState {
    data object Loading : CurrentUserPlaylistUiState
    data class Success(val data: List<PlaylistResponse>) : CurrentUserPlaylistUiState
}