package com.ke.music.fold.ui.components

import androidx.annotation.NonUiContext
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
internal fun SquareImageView(
    image: String,
    title: String,
    content: String?,
    onClick: () -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .padding(8.dp)
        .clickable {
            onClick()
        }) {
        AsyncImage(
            model = image,
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
            Text(text = title, color = Color.White, maxLines = 1, fontSize = 13.sp)

            if (content != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = content,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall, maxLines = 1,
                    fontSize = 11.sp
                )
            }
        }
    }

}