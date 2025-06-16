package com.android.internship.presentation.components.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.internship.R
import com.android.internship.presentation.components.CommonProgressIndicator

@Composable
fun NetworkStatusBanner(
    isNetworkAvailable: Boolean,
    isRefreshing: Boolean,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = !isNetworkAvailable,
        enter = expandVertically(animationSpec = tween(300)),
        exit = shrinkVertically(animationSpec = tween(300)),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (isRefreshing) {
                CommonProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    backgroundColor = Color.Transparent,
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.no_internet_connection),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )
                    TextButton(onClick = onRefreshClick) {
                        Text(stringResource(R.string.refresh))
                    }
                }
            }
        }
    }
}
