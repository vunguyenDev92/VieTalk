package com.android.internship.presentation.utils

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object FormatTimeStamp {
    fun messageTimeFormat(timestamp: Long): String {
        if (timestamp != 0.toLong()) {
            val now = Instant.now()
            val inputTime = Instant.ofEpochMilli(timestamp)

            val duration = Duration.between(inputTime, now)

            return if (duration.toHours() < 24) {
                val time = inputTime.atZone(ZoneId.systemDefault()).toLocalTime()
                time.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()))
            } else {
                val date = inputTime.atZone(ZoneId.systemDefault()).toLocalDate()
                date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault()))
            }
        }
        return ""
    }
}
