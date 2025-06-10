// file: com/android/internship/presentation/components/chat/TimeHeaderComponent.kt
package com.android.internship.presentation.components.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.internship.data.model.Message
import com.android.internship.presentation.components.MessageItem
import com.android.internship.presentation.theme.robotoFamily
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TimeHeaderComponent(
    item: MessageItem.TimeHeader,
    modifier: Modifier = Modifier,
) {
    val dateText = remember(item.timestamp) {
        formatDateHeader(item.timestamp)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = dateText,
            fontFamily = robotoFamily,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            color = Color(0xFF333333),
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
            val instant = Instant.ofEpochMilli(message.time.toLong())
            LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        } catch (e: Exception) {
            LocalDateTime.now()
        }
    }

    val timeText = remember(messageTime) {
        formatFullMessageTime(messageTime)
    }

    Box(
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = timeText,
            fontFamily = robotoFamily,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            color = Color(0xFF333333),
        )
    }
}

private fun formatDateHeader(timestamp: LocalDateTime): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val messageDate = timestamp.toLocalDate()

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
    val timeText = timestamp.format(timeFormatter)

    val formattedString = when {
        messageDate.isEqual(today) -> {
            "TODAY AT $timeText"
        }
        messageDate.isEqual(yesterday) -> {
            "YESTERDAY AT $timeText"
        }
        messageDate.isAfter(today.minusWeeks(1)) -> {
            val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)
            val dayOfWeekText = timestamp.format(dayOfWeekFormatter)
            "$dayOfWeekText AT $timeText"
        }
        else -> {
            val fullDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
            val fullDateText = timestamp.format(fullDateFormatter)
            "$fullDateText AT $timeText"
        }
    }

    return formattedString.uppercase(Locale.ENGLISH)
}

private fun formatFullMessageTime(timestamp: LocalDateTime): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val messageDate = timestamp.toLocalDate()

    val timeText = timestamp.format(DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH))

    return when {
        messageDate.isEqual(today) -> "TODAY AT $timeText"
        messageDate.isEqual(yesterday) -> "YESTERDAY AT $timeText"
        else -> {
            val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)
            val dayOfWeekText = timestamp.format(dayOfWeekFormatter)
            val fullDateText = timestamp.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH))
            "$dayOfWeekText AT $timeText"
        }
    }
}
