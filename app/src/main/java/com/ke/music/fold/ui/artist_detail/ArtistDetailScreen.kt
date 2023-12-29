package com.ke.music.fold.ui.artist_detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.music.api.response.AlbumResponse
import com.ke.music.api.response.ArtistDescResponse
import com.ke.music.api.response.ArtistResponse
import com.ke.music.api.response.MvResponse
import com.ke.music.api.response.SongResponse
import com.ke.music.fold.ui.components.LocalNavigationHandler
import com.ke.music.fold.ui.components.NavigationAction
import com.ke.music.fold.ui.components.Rectangle16V9ImageView
import com.ke.music.fold.ui.components.SongView
import com.ke.music.fold.ui.components.SquareImageView
import kotlinx.coroutines.launch

@Composable
internal fun ArtistDetailRoute(windowSizeClass: WindowSizeClass, closeDetailScreen: () -> Boolean) {
    val viewModel = hiltViewModel<ArtistDetailViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ArtistDetailScreen(windowSizeClass = windowSizeClass, uiState = uiState, {
        viewModel.refresh()
    }, closeDetailScreen)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ArtistDetailScreen(
    windowSizeClass: WindowSizeClass,
    uiState: ArtistDetailUiState,
    refresh: () -> Unit = {},
    closeDetailScreen: () -> Boolean,
) {

    val artistName = (uiState as? ArtistDetailUiState.Success)?.content?.artist?.name ?: ""

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = artistName) }, navigationIcon = {
            IconButton(onClick = { closeDetailScreen() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })
    }) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            when (uiState) {
                is ArtistDetailUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is ArtistDetailUiState.Success -> {

                    val scope = rememberCoroutineScope()

                    when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.Expanded -> {
                            //大屏幕的话把相似歌手固定在右边
                            Row(modifier = Modifier.fillMaxSize()) {

                                Column(
                                    modifier = Modifier
                                        .weight(0.7f)
                                        .fillMaxHeight()
                                ) {

                                    val tabs =
                                        listOf("热门作品", "所有专辑", "相关MV", "艺人介绍")
                                    val pageState = rememberPagerState {
                                        tabs.size
                                    }
                                    ScrollableTabRow(
                                        selectedTabIndex = pageState.currentPage,
                                        edgePadding = 0.dp,
                                        divider = {

                                        }
                                    ) {
                                        tabs.forEach { item ->
                                            Tab(
                                                selected = tabs.indexOf(item) == pageState.currentPage,
                                                onClick = {
                                                    scope.launch {
                                                        pageState.animateScrollToPage(
                                                            tabs.indexOf(
                                                                item
                                                            )
                                                        )
                                                    }
                                                },
                                                text = {
                                                    Text(text = item)
                                                }
                                            )
                                        }
                                    }

                                    HorizontalPager(
                                        state = pageState,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        when (it) {
                                            0 -> {
                                                ArtistHotSongsScreen(hotSongs = uiState.content.hotSongs)
                                            }

                                            1 -> {
                                                ArtistAlbumsScreen(albums = uiState.content.hotAlbums)
                                            }

                                            2 -> {
                                                ArtistMVsScreen(mvs = uiState.content.mvs)

                                            }

                                            3 -> {
                                                ArtistDescScreen(artistDescResponse = uiState.content.desc)
                                            }

//                                            4 -> {
//                                                ArtistSimiArtistsScreen(list = uiState.content.simiArtists)
//                                            }

                                            else -> {
                                                Text(text = it.toString())
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(
                                    modifier = Modifier
                                        .weight(0.3f)
                                        .fillMaxHeight()
                                ) {
                                    Text(
                                        text = "相似歌手",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = MaterialTheme.colorScheme.primary
                                        ),
                                        modifier = Modifier.padding(16.dp)
                                    )

                                    ArtistSimiArtistsScreen(list = uiState.content.simiArtists)
                                }
                            }
                        }

                        else -> {
                            val tabs =
                                listOf("热门作品", "所有专辑", "相关MV", "艺人介绍", "相似歌手")
                            val pageState = rememberPagerState {
                                tabs.size
                            }
                            Column(modifier = Modifier.fillMaxSize()) {
                                ScrollableTabRow(
                                    selectedTabIndex = pageState.currentPage,
                                    edgePadding = 0.dp,
                                    divider = {

                                    }
                                ) {
                                    tabs.forEach { item ->
                                        Tab(
                                            selected = tabs.indexOf(item) == pageState.currentPage,
                                            onClick = {
                                                scope.launch {
                                                    pageState.animateScrollToPage(tabs.indexOf(item))
                                                }
                                            },
                                            text = {
                                                Text(text = item)
                                            }
                                        )
                                    }
                                }

                                HorizontalPager(
                                    state = pageState,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    when (it) {
                                        0 -> {
                                            ArtistHotSongsScreen(hotSongs = uiState.content.hotSongs)
                                        }

                                        1 -> {
                                            ArtistAlbumsScreen(albums = uiState.content.hotAlbums)
                                        }

                                        2 -> {
                                            ArtistMVsScreen(mvs = uiState.content.mvs)

                                        }

                                        3 -> {
                                            ArtistDescScreen(artistDescResponse = uiState.content.desc)
                                        }

                                        4 -> {
                                            ArtistSimiArtistsScreen(list = uiState.content.simiArtists)
                                        }

                                        else -> {
                                            Text(text = it.toString())
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                is ArtistDetailUiState.Error -> {
                    OutlinedButton(onClick = {
                        refresh()
                    }) {
                        Text(text = "出错了，点我重试")
                    }
                }
            }

        }

    }
}

@Composable
private fun ArtistHotSongsScreen(hotSongs: List<SongResponse>) {

    LazyVerticalGrid(columns = GridCells.Adaptive(320.dp), modifier = Modifier.fillMaxSize()) {
        items(hotSongs, key = {
            it.id
        }) {
            SongView(songResponse = it)
        }
    }
}

@Composable
private fun ArtistAlbumsScreen(albums: List<AlbumResponse>) {
    LazyVerticalGrid(columns = GridCells.Adaptive(160.dp), modifier = Modifier.fillMaxSize()) {
        items(albums, key = {
            it.id
        }) {
            SquareImageView(image = it.imageUrl, title = it.name, content = null) {

            }
        }
    }
}


@Composable
private fun ArtistMVsScreen(mvs: List<MvResponse>) {
    val navigationHandler = LocalNavigationHandler.current
    LazyVerticalGrid(columns = GridCells.Adaptive(240.dp), modifier = Modifier.fillMaxSize()) {
        items(mvs, key = {
            it.id
        }) {
            Rectangle16V9ImageView(image = it.cover, title = it.name, content = null) {
                navigationHandler.navigate(NavigationAction.NavigateToMvDetail(it.id))
            }
        }
    }
}

@Composable
private fun ArtistSimiArtistsScreen(list: List<ArtistResponse>) {
    val navigationHandler = LocalNavigationHandler.current
    LazyVerticalGrid(columns = GridCells.Adaptive(120.dp), modifier = Modifier.fillMaxSize()) {
        items(list, key = {
            it.id
        }) {
            SquareImageView(image = it.avatar ?: "", title = it.name, content = null) {
                navigationHandler.navigate(NavigationAction.NavigateToArtistDetail(it.id))
            }
        }
    }
}


@Composable
private fun ArtistDescScreen(artistDescResponse: ArtistDescResponse) {

    val contentList = mutableListOf<Pair<String, Boolean>>()
    contentList.add(Pair("简介", true))
    contentList.add(artistDescResponse.briefDesc to false)

    artistDescResponse.introduction.forEach {
        contentList.add(it.ti to true)
        contentList.add(it.txt to false)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(contentList, key = {
            it
        }) {

            if (it.second) {
                Text(
                    text = it.first,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                Text(text = it.first, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


