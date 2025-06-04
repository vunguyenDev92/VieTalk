package com.android.internship.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.internship.presentation.theme.Black
import com.android.internship.presentation.theme.Grey
import com.android.internship.presentation.theme.GreyLight
import kotlinx.coroutines.delay

object CommonToastManager {
    const val LENGTH_SHORT = 3000L
    const val LENGTH_LONG = 5000L

    private var message by mutableStateOf<String?>(null)
    private var icon by mutableStateOf<Int?>(null)
    private var iconColor by mutableStateOf(Black)
    private var backgroundColor by mutableStateOf(GreyLight)
    private var borderColor by mutableStateOf(Grey)
    private var durationMillis by mutableLongStateOf(LENGTH_SHORT)
    private var onDismiss = {}

    fun makeToast(
        icon: Int? = null,
        iconColor: Color = Black,
        backgroundColor: Color = GreyLight,
        borderColor: Color = Grey,
        durationMillis: Long = LENGTH_SHORT,
        onDismiss: () -> Unit = {},
    ): CommonToastManager {
        this.icon = icon
        this.iconColor = iconColor
        this.backgroundColor = backgroundColor
        this.borderColor = borderColor
        this.durationMillis = durationMillis
        this.onDismiss = onDismiss
        return this
    }

    fun show(message: String) {
        this.message = message
    }

    @Composable
    fun ToastHost() {
        if (message != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Row(
                    modifier = Modifier.clip(shape = RoundedCornerShape(8.dp))
                        .defaultMinSize(minHeight = 44.dp)
                        .fillMaxWidth()
                        .background(color = backgroundColor)
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(8.dp),
                        ),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    icon?.let {
                        Icon(
                            painter = painterResource(it),
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.padding(horizontal = 10.dp),
                        )
                    }
                    Text(
                        text = message.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Black,
                        modifier = Modifier.weight(1f).padding(5.dp),
                    )
                }
            }

            LaunchedEffect(Unit) {
                delay(durationMillis)
                message = null
                onDismiss()
            }
        }
    }
}
