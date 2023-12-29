package com.ke.music.fold.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.ke.music.api.response.SongResponse


@Composable
internal fun SongView(songResponse: SongResponse) {


    val navigationHandler = LocalNavigationHandler.current

    var expanded by remember { mutableStateOf(false) }

    ListItem(headlineContent = {
        Text(text = songResponse.name, maxLines = 1)
    }, leadingContent = {
        AsyncImage(
            model = songResponse.album.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Crop
        )
    }, supportingContent = {
        Text(text = songResponse.subTitle, maxLines = 1)
    }, trailingContent = {
        Row {
            if (songResponse.mv != 0L) {
                IconButton(onClick = {
                    navigationHandler.navigate(
                        NavigationAction.NavigateToMvDetail(
                            songResponse.mv
                        )
                    )
                }) {
                    Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null)

                }
            }

            Box {
                IconButton(onClick = {
                    expanded = true
                }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(onClick = { expanded = false }, text = {
                        Text(text = "收藏到歌单")
                    })

                    DropdownMenuItem(onClick = {
                        expanded = false
                        navigationHandler.navigate(
                            NavigationAction.NavigateToComment(
                                songResponse.id,
                                0
                            )
                        )
                    }, text = {
                        Text(text = "评论")
                    })

                    DropdownMenuItem(onClick = { expanded = false }, text = {
                        Text(text = "分享")
                    })

                    songResponse.artists.forEach { artist ->
                        DropdownMenuItem(onClick = {
                            expanded = false
                            navigationHandler.navigate(
                                NavigationAction.NavigateToArtistDetail(
                                    artist.id
                                )
                            )
                        }, text = {
                            Text(text = "歌手：${artist.name}")
                        })
                    }
                    DropdownMenuItem(onClick = {
                        expanded = false
                        navigationHandler.navigate(
                            NavigationAction.NavigateToAlbumDetail(
                                songResponse.album.id
                            )
                        )
                    }, text = {
                        Text(text = "专辑：${songResponse.album.name}")

                    })

                }
            }
        }


    })
}