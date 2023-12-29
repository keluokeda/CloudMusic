package com.ke.music.fold.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.music.fold.observeWithLifecycle
import kotlinx.coroutines.launch

@Composable
internal fun SplashRoute(
    hasLogin: (Boolean) -> Unit,
) {
    val viewModel = hiltViewModel<SplashViewModel>()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val showErrorView by viewModel.showErrorView.collectAsStateWithLifecycle()

    viewModel.checkLoginResult.observeWithLifecycle {
        when (it) {
            is CheckLoginResult.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(it.message)
                }
            }

            is CheckLoginResult.Logged -> {
                hasLogin(true)
            }

            is CheckLoginResult.NoLogin -> {
                hasLogin(false)
            }
        }
    }

    SplashScreen(
        snackbarHostState, showErrorView
    ) {
        viewModel.checkLogin()
    }
}


@Composable
private fun SplashScreen(
    snackbarHostState: SnackbarHostState,
    showErrorView: Boolean,
    checkLogin: () -> Unit,
) {
    Scaffold(snackbarHost = {
        SnackbarHost(snackbarHostState)
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            if (showErrorView) {
                OutlinedButton(onClick = { checkLogin() }) {
                    Text(text = "出错了，点我重试")
                }
            } else {
                CircularProgressIndicator()
            }

        }
    }
}