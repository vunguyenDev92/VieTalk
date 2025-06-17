package com.android.internship.presentation.components.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.internship.R
import com.android.internship.presentation.components.CommonProgressIndicator
import kotlinx.coroutines.delay

@Composable
fun NetworkStatusBanner(
    isNetworkAvailable: Boolean,
    isRefreshing: Boolean,
    onRefreshClick: () -> Unit,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    var showProgressIndicator by remember { mutableStateOf(false) }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            showProgressIndicator = true
            delay(5000)
            showProgressIndicator = false
        } else {
            showProgressIndicator = false
        }
    }

    AnimatedVisibility(
        visible = !isNetworkAvailable || errorMessage != null,
        enter = expandVertically(animationSpec = tween(300)),
        exit = shrinkVertically(animationSpec = tween(300)),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (showProgressIndicator && isRefreshing) {
                CommonProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    backgroundColor = Color.Transparent,
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = when {
                            errorMessage != null -> errorMessage
                            !isNetworkAvailable -> stringResource(R.string.no_internet_connection)
                            else -> stringResource(R.string.no_internet_connection)
                        },
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )

                    if (!isNetworkAvailable) {
                        Text(
                            text = stringResource(R.string.refresh),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                ) { onRefreshClick() }
                                .padding(8.dp),
                        )
                    }
                }
            }
        }
    }
}
