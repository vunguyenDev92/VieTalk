package com.android.internship.presentation.components.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.internship.presentation.components.CommonProgressIndicator

@Composable
fun NetworkErrorComponent(
    isRefreshing: Boolean,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (isRefreshing) {
            CommonProgressIndicator()
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "No internet connection!",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge,
                )
                TextButton(onClick = onRefreshClick) {
                    Text("Refresh")
                }
            }
        }
    }
}
