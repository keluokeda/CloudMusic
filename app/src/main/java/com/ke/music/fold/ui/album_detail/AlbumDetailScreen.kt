package com.ke.music.fold.ui.album_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.music.api.response.AlbumDetailResponse
import com.ke.music.fold.entity.UiState
import com.ke.music.fold.ui.components.SongView


@Composable
internal fun AlbumDetailRoute(windowSizeClass: WindowSizeClass, onClose: () -> Unit) {
    val viewModel = hiltViewModel<AlbumDetailViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AlbumDetailScreen(
        windowSizeClass = windowSizeClass,
        uiState = uiState,
        onClose = onClose
    ) {
        viewModel.refresh()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumDetailScreen(
    windowSizeClass: WindowSizeClass,
    uiState: UiState<AlbumDetailResponse>,
    onClose: () -> Unit,
    refresh: () -> Unit,
) {
    val title = (uiState as? UiState.Success<AlbumDetailResponse>)?.content?.name ?: ""

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = title)
            }, navigationIcon = {
                IconButton(onClick = { onClose() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
        }
    ) { padding ->
        when (uiState) {
            UiState.Error -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    OutlinedButton(onClick = { refresh() }) {
                        Text(text = "出错了，点我重试")
                    }
                }
            }

            UiState.Loading -> Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is UiState.Success -> {
                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        LazyColumn(
                            modifier = Modifier
                                .padding(padding)
                                .padding(16.dp)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            item {
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 32.dp)
                                        .padding(top = 32.dp, bottom = 16.dp)
                                        .aspectRatio(1f)
                                ) {
                                    AsyncImage(
                                        model = uiState.content.image,
                                        contentDescription = null
                                    )
                                }
                            }



                            item {
                                TextButton(onClick = { /*TODO*/ }) {
                                    Text(text = uiState.content.artists.first().name)
                                }
                            }

                            val description = uiState.content.description

                            if (description != null) {
                                item {
                                    Text(text = description)
                                }
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

                            items(uiState.content.songs, key = {
                                it.id
                            }) {
                                SongView(songResponse = it)
                            }

                        }
                    }

                    else -> {

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .width(300.dp)
                                    .padding(16.dp)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 32.dp)
                                            .padding(top = 32.dp, bottom = 16.dp)
                                            .aspectRatio(1f)
                                    ) {
                                        AsyncImage(
                                            model = uiState.content.image,
                                            contentDescription = null
                                        )
                                    }
                                }



                                item {
                                    TextButton(onClick = { /*TODO*/ }) {
                                        Text(text = uiState.content.artists.first().name)
                                    }
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

                                val description = uiState.content.description

                                if (description != null) {
                                    item {
                                        Text(text = description)
                                    }
                                }
                            }

                            LazyVerticalGrid(columns = GridCells.Adaptive(320.dp)) {
                                items(uiState.content.songs, key = {
                                    it.id
                                }) {
                                    SongView(songResponse = it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}