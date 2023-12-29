package com.ke.music.fold.ui.mv_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.api.response.BaseResponse
import com.ke.music.api.response.MvDetailResponse
import com.ke.music.fold.domain.GetMvDetailUseCase
import com.ke.music.fold.entity.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MvDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMvDetailUseCase: GetMvDetailUseCase,
) : ViewModel() {

    private val mvId = savedStateHandle.get<Long>("id")!!

    private val _uiState = MutableStateFlow<MvDetailUiState>(MvDetailUiState.Loading)

    internal val uiState: StateFlow<MvDetailUiState> = _uiState


    init {
        refresh()
    }

    internal fun refresh() {
        viewModelScope.launch {
            _uiState.value = MvDetailUiState.Loading
            val response = getMvDetailUseCase(mvId).successOr(BaseResponse.defaultError())

            if (response.success) {
                _uiState.value = MvDetailUiState.Success(response.data!!)
            } else {
                _uiState.value = MvDetailUiState.Error

            }
        }
    }
}

internal sealed interface MvDetailUiState {
    data object Loading : MvDetailUiState

    data class Success(val content: MvDetailResponse) : MvDetailUiState

    data object Error : MvDetailUiState
}