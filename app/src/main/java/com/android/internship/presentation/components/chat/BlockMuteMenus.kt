package com.android.internship.presentation.components.chat

import android.os.Build.VERSION_CODES.Q
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.internship.R

enum class MuteDuration {
    MINUTES_30,
    HOUR_1,
    DAY_1,
    INDEFINITELY,
}

@Composable
fun BlockMuteMenus(
    isMuted: Boolean = false,
    isBlocked: Boolean = false,
    onMuteClick: (duration: MuteDuration) -> Unit = {},
    onBlockClick: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    var onMute by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp),
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.more_options),
            )
        }
        if (onMute == false) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Color.White)
                    .advancedShadow(),
            ) {
                DropdownMenuItem(
                    text = {
                        Row {
                            Icon(
                                painter = if (isMuted) {
                                    painterResource(R.drawable.ic_mute)
                                } else {
                                    painterResource(
                                        R.drawable.ic_unmute,
                                    )
                                },
                                contentDescription = stringResource(R.string.mute_notification),
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(24.dp),
                            )
                            Text(
                                text = if (isMuted) {
                                    stringResource(R.string.unmute_notifications)
                                } else {
                                    stringResource(
                                        R.string.mute_notifications,
                                    )
                                },
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(start = 8.dp),
                                fontWeight = FontWeight.W300,
                                fontSize = 14.sp,
                            )
                        }
                    },
                    onClick = {
                        onMute = true
                    },
                )
                DropdownMenuItem(
                    text = {
                        Row {
                            Icon(
                                painter = painterResource(R.drawable.ic_block),
                                contentDescription = "Block",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(24.dp),
                            )
                            Text(
                                text = if (isBlocked) {
                                    stringResource(R.string.unblock)
                                } else {
                                    stringResource(
                                        R.string.block,
                                    )
                                },
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 8.dp),
                                fontWeight = FontWeight.W300,
                                fontSize = 14.sp,
                            )
                        }
                    },
                    onClick = {
                        onBlockClick()
                        expanded = false
                    },
                )
            }
        } else {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .background(Color.White)
                    .padding(top = 10.dp, bottom = 10.dp, start = 5.dp, end = 40.dp),
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string._30_minutes),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(start = 8.dp),
                            fontWeight = FontWeight.W300,
                            fontSize = 14.sp,
                        )
                    },
                    onClick = {
                        onMuteClick(MuteDuration.MINUTES_30)
                        expanded = false
                        onMute = false
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string._1_hour),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(start = 8.dp),
                            fontWeight = FontWeight.W300,
                            fontSize = 14.sp,
                        )
                    },
                    onClick = {
                        onMuteClick(MuteDuration.HOUR_1)
                        expanded = false
                        onMute = false
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string._1_day),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(start = 8.dp),
                            fontWeight = FontWeight.W300,
                            fontSize = 14.sp,
                        )
                    },
                    onClick = {
                        onMuteClick(MuteDuration.DAY_1)
                        expanded = false
                        onMute = false
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.util_turned_back_on),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(start = 8.dp),
                            fontWeight = FontWeight.W300,
                            fontSize = 14.sp,
                        )
                    },
                    onClick = {
                        onMuteClick(MuteDuration.INDEFINITELY)
                        expanded = false
                        onMute = false
                    },
                )
            }
        }
    }
}

@RequiresApi(Q)
fun Modifier.advancedShadow(
    color: Color = Color.Black,
    alpha: Float = 0.25f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 4.dp,
    offsetX: Dp = 0.dp,
) = drawBehind {
    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowBlurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor,
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint,
        )
    }
}
