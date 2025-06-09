package com.android.internship.presentation.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.internship.data.model.Message
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TimeHeaderComponent(
    timestamp: LocalDateTime,
    modifier: Modifier = Modifier,
) {
    val dateText = remember(timestamp) {
        formatDateHeader(timestamp)
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = dateText,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 12.dp, vertical = 4.dp),
        )
    }
}

@Composable
fun MessageTimeHeaderComponent(
    message: Message,
    modifier: Modifier = Modifier,
) {
    val messageTime = remember(message.time) {
        try {
            LocalDateTime.parse(message.time)
        } catch (e: Exception) {
            LocalDateTime.now()
        }
    }

    val timeText = remember(messageTime) {
        formatFullMessageTime(messageTime)
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = timeText,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 12.dp, vertical = 4.dp),
        )
    }
}

private fun formatDateHeader(timestamp: LocalDateTime): String {
    val today = LocalDate.now()
    val messageDate = timestamp.toLocalDate()

    return when {
        messageDate == today -> "Today"
        messageDate == today.minusDays(1) -> "Yesterday"
        else -> timestamp.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }
}

private fun formatFullMessageTime(timestamp: LocalDateTime): String {
    val today = LocalDate.now()
    val messageDate = timestamp.toLocalDate()

    return when {
        messageDate == today -> "Today ${timestamp.format(DateTimeFormatter.ofPattern("h:mm a"))}"
        messageDate == today.minusDays(1) -> "Yesterday ${timestamp.format(DateTimeFormatter.ofPattern("h:mm a"))}"
        else -> timestamp.format(DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a"))
    }
}
