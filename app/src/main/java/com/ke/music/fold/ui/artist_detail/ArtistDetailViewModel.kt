package com.ke.music.fold.ui.artist_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.api.response.ArtistDetailResponse
import com.ke.music.api.response.BaseResponse
import com.ke.music.fold.domain.GetArtistDetailUseCase
import com.ke.music.fold.entity.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ArtistDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getArtistDetailUseCase: GetArtistDetailUseCase,
) : ViewModel() {
    private val artistId = savedStateHandle.get<Long>("id")!!

    private val _uiState = MutableStateFlow<ArtistDetailUiState>(ArtistDetailUiState.Loading)

    internal val uiState: StateFlow<ArtistDetailUiState>
        get() = _uiState


    init {
        refresh()
    }

    internal fun refresh() {
        viewModelScope.launch {
            _uiState.value = ArtistDetailUiState.Loading
            val response = getArtistDetailUseCase(artistId).successOr(BaseResponse.defaultError())
            if (response.success) {
                _uiState.value = ArtistDetailUiState.Success(response.data!!)
            } else {
                _uiState.value = ArtistDetailUiState.Error(response.message)
            }

        }
    }
}

internal sealed interface ArtistDetailUiState {
    data object Loading : ArtistDetailUiState
    data class Success(val content: ArtistDetailResponse) : ArtistDetailUiState

    data class Error(val message: String) : ArtistDetailUiState
}