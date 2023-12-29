package com.ke.music.fold.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.api.response.BaseResponse
import com.ke.music.fold.domain.CheckLoginStatusUseCase
import com.ke.music.fold.entity.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(private val checkLoginStatusUseCase: CheckLoginStatusUseCase) :
    ViewModel() {

    private val _checkLoginResult = Channel<CheckLoginResult>(capacity = Channel.CONFLATED)

    internal val checkLoginResult = _checkLoginResult.receiveAsFlow()


    private val _showErrorView = MutableStateFlow(false)
    internal val showErrorView: StateFlow<Boolean>
        get() = _showErrorView
    init {
        checkLogin()
    }



    internal fun checkLogin() {
        viewModelScope.launch {
            _showErrorView.value = false
            val result = checkLoginStatusUseCase(Unit).successOr(BaseResponse.defaultError())
            if (result.success) {
                _checkLoginResult.send(CheckLoginResult.Logged)
            } else if (result.code == 401) {
                _checkLoginResult.send(CheckLoginResult.NoLogin)
            } else {
                _checkLoginResult.send(CheckLoginResult.Error(result.message))
                _showErrorView.value = true
            }

        }
    }
}

sealed interface CheckLoginResult {
    data object NoLogin : CheckLoginResult
    data object Logged : CheckLoginResult
    data class Error(val message: String) : CheckLoginResult
}