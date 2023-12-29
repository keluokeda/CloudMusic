package com.ke.music.fold.ui.user_playlists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.music.api.response.PlaylistResponse


@Composable
internal fun UserPlaylistsRoute(
    windowSizeClass: WindowSizeClass,
    onPlaylistItemClick: (Long) -> Unit,
) {

    val viewModel = hiltViewModel<UserPlaylistsViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UserPlaylistsScreen(
        uiState = uiState,
        windowSizeClass = windowSizeClass,
        onPlaylistItemClick = onPlaylistItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserPlaylistsScreen(
    uiState: CurrentUserPlaylistUiState,
    windowSizeClass: WindowSizeClass,

    onPlaylistItemClick: (Long) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "我的歌单")
            }, actions = {
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            })
        }
    ) { padding ->
        when (uiState) {
            CurrentUserPlaylistUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is CurrentUserPlaylistUiState.Success -> {

                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        LazyColumn(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize()
                        ) {
                            items(uiState.data, key = { it.id }) {
                                ListPlaylistItem(
                                    playlistResponse = it,
                                    onPlaylistItemClick = onPlaylistItemClick
                                )

                            }
                        }
                    }

                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(160.dp),
                            modifier = Modifier.padding(padding),
                            contentPadding = PaddingValues(16.dp),
                        ) {
                            items(uiState.data,
                                key = { it.id }) {
                                GridPlaylistItem(
                                    playlistResponse = it,
                                    onPlaylistItemClick = onPlaylistItemClick
                                )
                            }
                        }
                    }
                }

            }
        }

    }
}


@Composable
internal fun GridPlaylistItem(
    playlistResponse: PlaylistResponse,
    onPlaylistItemClick: (Long) -> Unit,
) {

    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .padding(8.dp)
        .clickable {
            onPlaylistItemClick(playlistResponse.id)
        }) {
        AsyncImage(
            model = playlistResponse.coverImgUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Color.Black.copy(
                        alpha = 0.3f
                    )
                )
                .padding(4.dp)
        ) {
            Text(text = playlistResponse.name, color = Color.White, maxLines = 1, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${playlistResponse.trackCount}首",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall, maxLines = 1,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
internal fun ListPlaylistItem(
    playlistResponse: PlaylistResponse,
    onPlaylistItemClick: (Long) -> Unit,
) {
    ListItem(headlineContent = {
        Text(text = playlistResponse.name, maxLines = 1)
    }, supportingContent = {
        Text(text = "${playlistResponse.trackCount}首", maxLines = 1)
    }, leadingContent = {

        AsyncImage(
            model = playlistResponse.coverImgUrl,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Crop
        )
    }, modifier = Modifier.clickable {
        onPlaylistItemClick(playlistResponse.id)
    })
}








