package com.android.internship.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.internship.R
import com.android.internship.presentation.theme.White

@Composable
fun CommonAvatar(
    avatar: String?,
    modifier: Modifier = Modifier,
    isOnline: Boolean = false,
) {
    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        CommonCircleAsyncImage(
            url = avatar,
            contentDescription = stringResource(R.string.avatar),
            modifier = Modifier.fillMaxSize(),
        )
        if (isOnline) {
            Canvas(
                modifier = Modifier,
            ) {
                drawCircle(
                    color = White,
                    radius = 6.dp.toPx(),
                    center = Offset(
                        this.size.width - 8.dp.toPx(),
                        this.size.height - 8.dp.toPx(),
                    ),
                )
                drawCircle(
                    color = Color(0xff1ba505),
                    radius = 5.dp.toPx(),
                    center = Offset(
                        this.size.width - 8.dp.toPx(),
                        this.size.height - 8.dp.toPx(),
                    ),
                )
            }
        }
    }
}
