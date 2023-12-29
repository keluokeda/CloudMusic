package com.ke.music.fold.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUpOffAlt
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.ke.music.fold.db.entity.Comment

@Composable
internal fun CommentRoute(windowSizeClass: WindowSizeClass, onClose: () -> Unit) {
    val viewModel = hiltViewModel<CommentViewModel>()
    val list = viewModel.comments.collectAsLazyPagingItems()
    val childComments = viewModel.childComments.collectAsLazyPagingItems()

    CommentScreen(windowSizeClass = windowSizeClass, list = list, onClose, childComments) {
        viewModel.onMoreCommentClick(it)
        childComments.refresh()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommentScreen(
    windowSizeClass: WindowSizeClass,
    list: LazyPagingItems<Comment>,
    onClose: () -> Unit,
    childComments: LazyPagingItems<Comment>,
    onMoreCommentClicked: (Long) -> Unit,
) {


    val splitFraction = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            if (childComments.itemCount == 0) {
                1f
            } else {
                0f
            }
        }

        else -> 0.5f
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "评论") }, navigationIcon = {
                IconButton(onClick = {
                    if (childComments.itemCount == 0) {
                        onClose()
                    } else {
                        onMoreCommentClicked(0L)
                    }
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
        }
    ) { paddingValues: PaddingValues ->


        TwoPane(
            first = {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(list.itemCount, key = {
                        list[it]?.commentId ?: 0
                    }) {

                        val comment = list[it]!!

                        CommentView(comment = comment) {
                            onMoreCommentClicked(comment.commentId)
                        }

                    }
                }
            },
            second = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
//                        .background(color = Color.Cyan)
                ) {


                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(childComments.itemCount, key = {
                            childComments[it]?.commentId ?: 0
                        }) {

                            val comment = childComments[it]!!

                            CommentView(comment = comment) {
//                                onMoreCommentClicked(comment.commentId)
                            }

                        }
                    }

//                    if (selectedComment != null) {
//                        CommentView(comment = selectedComment!!) {
//                            Logger.d("评论id是 ${selectedComment!!.commentId}")
//                        }
//                    } else {
//                        Text(text = "请选择评论")
//                    }
                }
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction),
            displayFeatures = listOf(),
            modifier = Modifier.padding(paddingValues)
        )


    }

}

@Composable
private fun CommentView(comment: Comment, onMoreCommentClicked: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        AsyncImage(
            model = comment.userAvatar,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = comment.username)
            Text(
                "${comment.timeString} ${comment.ipLocation}",
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )

            Text(text = comment.content ?: "")

            if (comment.replyCount != 0) {
                OutlinedButton(onClick = { onMoreCommentClicked() }) {
                    Text(text = "${comment.replyCount}条回复")
                }

            }
        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.ThumbUpOffAlt, contentDescription = null)
        }
    }
}