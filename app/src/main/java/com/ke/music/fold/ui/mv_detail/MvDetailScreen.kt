package com.ke.music.fold.ui.mv_detail

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.exoplayer.analytics.AnalyticsListener
import com.ke.music.fold.ui.components.Rectangle16V9ImageView
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.controller.VideoPlayerControllerConfig
import io.sanghun.compose.video.uri.VideoPlayerMediaItem

@Composable
internal fun MvDetailRoute(windowSizeClass: WindowSizeClass, closeDetailScreen: () -> Unit) {

    val viewModel = hiltViewModel<MvDetailViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MvDetailScreen(
        windowSizeClass = windowSizeClass,
        uiState = uiState,
        closeDetailScreen = closeDetailScreen,
        refresh = {
            viewModel.refresh()
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MvDetailScreen(
    windowSizeClass: WindowSizeClass,
    uiState: MvDetailUiState,
    closeDetailScreen: () -> Unit,
    refresh: () -> Unit,
) {

    val title = (uiState as? MvDetailUiState.Success)?.content?.name ?: ""
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = title) }, navigationIcon = {
                IconButton(onClick = { closeDetailScreen() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
        }
    ) { padding ->

        when (uiState) {
            MvDetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center

                ) {
                    OutlinedButton(onClick = {
                        refresh()
                    }) {
                        Text(text = "出错了，点我重试")
                    }
                }
            }

            MvDetailUiState.Loading -> Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is MvDetailUiState.Success -> {
                when (windowSizeClass.widthSizeClass) {


                    WindowWidthSizeClass.Expanded -> {

                        Row(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize()
                        ) {

                            LazyColumn(
                                modifier = Modifier
                                    .weight(3f)
                                    .fillMaxSize()
                            ) {

                                item {
                                    VideoPlayerView(url = uiState.content.url)
                                }

                                item {
                                    MvDetailActionView(
                                        subCount = uiState.content.subCount,
                                        shareCount = uiState.content.shareCount,
                                        commentCount = uiState.content.commentCount
                                    )
                                }
                            }

                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            ) {

                                item {
                                    Text(
                                        text = "相关MV",
                                        style = MaterialTheme.typography.headlineMedium,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }

                                simiMvList(uiState)
                            }
                        }
                    }

                    else -> {

                        Column(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize()
                        ) {

                            VideoPlayerView(url = uiState.content.url)


                            MvDetailActionView(
                                subCount = uiState.content.subCount,
                                shareCount = uiState.content.shareCount,
                                commentCount = uiState.content.commentCount
                            )


                            Text(
                                text = "相关MV",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(16.dp)
                            )

                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(160.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {


                                simiMvList(uiState)
                            }
                        }

                    }


                }
            }
        }

    }
}

@Composable
private fun MvDetailActionView(
    subCount: Int,
    shareCount: Int,
    commentCount: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        OutlinedButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Star, contentDescription = null)
            Text(text = subCount.toString())
        }

        OutlinedButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null)
            Text(text = shareCount.toString())
        }

        OutlinedButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Comment, contentDescription = null)
            Text(text = commentCount.toString())
        }

    }
}

private fun LazyGridScope.simiMvList(uiState: MvDetailUiState.Success) {
    items(uiState.content.simiMvs, key = {
        it.id
    }) {
        Rectangle16V9ImageView(
            image = it.cover,
            title = it.name,
            content = null
        ) {

        }
    }
}

private fun LazyListScope.simiMvList(uiState: MvDetailUiState.Success) {
    items(uiState.content.simiMvs, key = {
        it.id
    }) {
        Rectangle16V9ImageView(
            image = it.cover,
            title = it.name,
            content = null
        ) {

        }
    }
}

@Composable
private fun VideoPlayerView(url: String) {
    val activity = LocalContext.current as Activity


    val start by remember {
        mutableIntStateOf(activity.requestedOrientation)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
    ) {


        VideoPlayer(
            mediaItems = listOf(

                VideoPlayerMediaItem.NetworkMediaItem(
                    url
                )
            ),
            handleLifecycle = true,
            autoPlay = true,
            usePlayerController = true,
            enablePip = false,
            handleAudioFocus = true,
            controllerConfig = VideoPlayerControllerConfig(
                showSpeedAndPitchOverlay = false,
                showSubtitleButton = false,
                showCurrentTimeAndTotalTime = true,
                showBufferingProgress = false,
                showForwardIncrementButton = true,
                showBackwardIncrementButton = true,
                showBackTrackButton = true,
                showNextTrackButton = true,
                showRepeatModeButton = true,
                controllerShowTimeMilliSeconds = 5_000,
                controllerAutoShow = true,
                showFullScreenButton = true,
            ),
            onFullScreenExit = {
                activity.requestedOrientation = start
            },
            volume = 0.5f,  // volume 0.0f to 1.0f
            repeatMode = RepeatMode.ONE,       // or RepeatMode.ALL, RepeatMode.ONE
            onCurrentTimeChanged = { // long type, current player time (millisec)
            },
            playerInstance = { // ExoPlayer instance (Experimental)
                addAnalyticsListener(
                    object : AnalyticsListener {
                        // player logger
                    }
                )
            },
            modifier = Modifier
                .fillMaxSize()

        )
    }
}