package com.github.anamespace.todaylist.ui

import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

object UiConstants {
     val CARD_ROW_HEIGHT = 100.dp

     val CARD_TIME_FORMAT: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .toFormatter()

     val CARD_DATE_FORMAT: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .appendLiteral('.')
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendLiteral('.')
        .appendValue(ChronoField.YEAR, 4)
        .toFormatter()
}
