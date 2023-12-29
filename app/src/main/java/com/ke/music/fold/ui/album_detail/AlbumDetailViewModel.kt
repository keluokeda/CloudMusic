package com.ke.music.fold.ui.album_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.api.response.AlbumDetailResponse
import com.ke.music.api.response.BaseResponse
import com.ke.music.fold.domain.GetAlbumDetailUseCase
import com.ke.music.fold.entity.UiState
import com.ke.music.fold.entity.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AlbumDetailViewModel @Inject constructor(
    private val getAlbumDetailUseCase: GetAlbumDetailUseCase,
    savedStateHandle: SavedStateHandle,
) :
    ViewModel() {

    private val id = savedStateHandle.get<Long>("id")!!

    private val _uiState = MutableStateFlow<UiState<AlbumDetailResponse>>(UiState.Loading)

    internal val uiState: StateFlow<UiState<AlbumDetailResponse>> = _uiState


    init {
        refresh()
    }

    internal fun refresh() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = getAlbumDetailUseCase(id).successOr(BaseResponse.defaultError())
            if (result.success) {
                _uiState.value = UiState.Success(result.data!!)
            } else {
                _uiState.value = UiState.Error
            }
        }
    }

}

