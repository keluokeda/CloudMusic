package com.ke.music.fold.ui.playlist_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.music.api.response.PlaylistDetailResponse
import com.ke.music.fold.ui.components.SongView


@Composable
fun PlaylistDetailRoute(windowSizeClass: WindowSizeClass, closeDetailScreen: () -> Unit) {
    val viewModel = hiltViewModel<PlaylistDetailViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PlaylistDetailScreen(uiState = uiState, windowSizeClass = windowSizeClass, retry = {
        viewModel.refresh()
    }, closeDetailScreen = closeDetailScreen)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaylistDetailScreen(
    uiState: PlaylistDetailUiState,
    windowSizeClass: WindowSizeClass,
    closeDetailScreen: () -> Unit,
    retry: () -> Unit = {},
) {

    Scaffold(
        topBar = {
            TopAppBar(title = { }, navigationIcon = {
                IconButton(onClick = {
                    closeDetailScreen()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                PlaylistDetailUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is PlaylistDetailUiState.Success -> {
                    PlaylistDetailContent(
                        playlistDetailResponse = uiState.content,
                        windowSizeClass = windowSizeClass
                    )
                }

                is PlaylistDetailUiState.Error -> {
                    OutlinedButton(onClick = { retry() }) {
                        Text(text = "出错了，点我重试")
                    }
                }
            }
        }

    }

}

@Composable
private fun PlaylistDetailContent(
    playlistDetailResponse: PlaylistDetailResponse,
    windowSizeClass: WindowSizeClass,
) {

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            CompactContent(playlistDetailResponse = playlistDetailResponse)
        }

        else -> {
            MediumContent(playlistDetailResponse = playlistDetailResponse)
        }

    }
}

@Composable
private fun MediumContent(playlistDetailResponse: PlaylistDetailResponse) {

    Row(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .width(320.dp)

                .fillMaxHeight()
        ) {
            item {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    val size = maxWidth - 64.dp
                    AsyncImage(
                        model = playlistDetailResponse.playlist.coverImgUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(size)

                            .align(Alignment.Center)
                    )
                }
            }


            item {
                Text(
                    text = playlistDetailResponse.playlist.name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedIconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null)
                    }

                    OutlinedIconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Comment, contentDescription = null)
                    }

                    OutlinedIconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null)
                    }
                }

            }

            item {
                Text(
                    text = playlistDetailResponse.playlist.description ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        LazyVerticalGrid(columns = GridCells.Adaptive(320.dp)) {
            items(playlistDetailResponse.songs, key = {
                it.id
            }) {
                SongView(songResponse = it)
            }
        }
    }
}

@Composable
private fun CompactContent(playlistDetailResponse: PlaylistDetailResponse) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()

    ) {
        item {
            BoxWithConstraints(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)) {
                val size = maxWidth - 64.dp
                AsyncImage(
                    model = playlistDetailResponse.playlist.coverImgUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)

                        .align(Alignment.Center)
                )
            }
        }


        item {
            Text(
                text = playlistDetailResponse.playlist.name,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        item {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedIconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null)
                }

                OutlinedIconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Comment, contentDescription = null)
                }

                OutlinedIconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null)
                }
            }

        }

        items(playlistDetailResponse.songs, key = {
            it.id
        }) {
            SongView(songResponse = it)
        }
    }
}