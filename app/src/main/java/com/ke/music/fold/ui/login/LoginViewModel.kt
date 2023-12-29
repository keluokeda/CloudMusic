package com.ke.music.fold.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.api.response.BaseResponse
import com.ke.music.fold.domain.CreateLoginKeyUseCase
import com.ke.music.fold.domain.LoginUseCase
import com.ke.music.fold.entity.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val createLoginKeyUseCase: CreateLoginKeyUseCase,
    private val loginUseCase: LoginUseCase,
) :
    ViewModel() {

    private val _qrUrl = MutableStateFlow<QRUrlStatus>(QRUrlStatus.Loading)

    internal val qrUrl: StateFlow<QRUrlStatus>
        get() = _qrUrl

    private var key = ""


    private val _loginResult = Channel<LoginResult>(capacity = Channel.CONFLATED)

    internal val loginResult: Flow<LoginResult> = _loginResult.receiveAsFlow()

    init {
        refresh()
    }

    private val _loading = MutableStateFlow(false)

    internal val loading: StateFlow<Boolean>
        get() = _loading


    internal fun refresh() {
        viewModelScope.launch {
            _qrUrl.value = QRUrlStatus.Loading
            val result = createLoginKeyUseCase(Unit).successOr(BaseResponse.defaultError())

            if (result.success) {
                _qrUrl.value = QRUrlStatus.Success(result.data?.url ?: "")
            } else {
                _qrUrl.value = QRUrlStatus.Error
            }
            key = result.data?.key ?: ""
        }
    }

    internal fun login() {
        viewModelScope.launch {
            _loading.value = true

            val result = loginUseCase(key).successOr(BaseResponse.defaultError())

            if (result.success) {
                _loginResult.send(LoginResult.Success)
            } else {
                _loginResult.send(LoginResult.Error(result.message))
            }

            _loading.value = false
        }
    }
}

internal sealed interface QRUrlStatus {
    data object Loading : QRUrlStatus
    data class Success(val url: String) : QRUrlStatus
    data object Error : QRUrlStatus

    companion object {
        internal fun fromUrl(url: String?): QRUrlStatus {
            return if (url == null) {
                Loading
            } else if (url.isEmpty()) {
                Error
            } else {
                Success(url)
            }
        }
    }
}

/**
 * 登录结果
 */
internal sealed interface LoginResult {
    /**
     * 登录成功
     */
    data object Success : LoginResult

    /**
     * 登录失败
     */
    data class Error(val message: String) : LoginResult
}