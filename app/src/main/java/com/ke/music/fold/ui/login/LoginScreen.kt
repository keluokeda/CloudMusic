package com.ke.music.fold.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.music.fold.observeWithLifecycle
import com.lightspark.composeqr.QrCodeView
import kotlinx.coroutines.launch

@Composable
internal fun LoginRoute(
    onLoginSuccess: () -> Unit,
) {

    val viewModel = hiltViewModel<LoginViewModel>()

    val qrUrlStatus by viewModel.qrUrl.collectAsStateWithLifecycle()

    val loading by viewModel.loading.collectAsStateWithLifecycle()


    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.loginResult.observeWithLifecycle {
        when (it) {
            is LoginResult.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(it.message)
                }
            }

            LoginResult.Success -> {
                onLoginSuccess()
            }
        }
    }
    LoginScreen(qrUrlStatus = qrUrlStatus, refresh = {
        viewModel.refresh()
    }, login = {
        viewModel.login()

    }, loading = loading, snackbarHostState = snackbarHostState)


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen(
    qrUrlStatus: QRUrlStatus,
    refresh: () -> Unit = {},
    login: () -> Unit = {},
    loading: Boolean = false,
    size: Int = 250,
    snackbarHostState: SnackbarHostState? = null,
) {

    val state = snackbarHostState ?: remember { SnackbarHostState() }


    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "扫码登录") })
    }, snackbarHost = {
        SnackbarHost(hostState = state)
    }) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Gray)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            QrContentView(qrUrlStatus = qrUrlStatus, size = size)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    login()
                },
                enabled = !loading && qrUrlStatus is QRUrlStatus.Success,
                modifier = Modifier.width(size.dp)
            ) {
                Text(text = "我已扫码")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(onClick = {
                refresh()
            }, enabled = !loading, modifier = Modifier.width(size.dp)) {
                Text(text = "刷新二维码")
            }
        }

    }

}

@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun LoginScreenLoadingPreview() {
    LoginScreen(qrUrlStatus = QRUrlStatus.Loading)
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun LoginScreenIdlePreview() {
    LoginScreen(qrUrlStatus = QRUrlStatus.Success("https://www.baidu.com"))
}


@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun LoginScreenErrorPreview() {
    LoginScreen(qrUrlStatus = QRUrlStatus.Error)
}

@Composable
private fun QrContentView(
    qrUrlStatus: QRUrlStatus,
    size: Int = 250,
) {
    Box(modifier = Modifier.size(size.dp), contentAlignment = Alignment.Center) {
        when (qrUrlStatus) {
            QRUrlStatus.Error -> Text(text = "出错了，刷新试试")
            QRUrlStatus.Loading -> CircularProgressIndicator()
            is QRUrlStatus.Success -> QrCodeView(
                data = qrUrlStatus.url,
                modifier = Modifier.size(size.dp)
            )
        }
    }

}

