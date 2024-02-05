package com.aitgacem.budgeter.ui

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

fun String.toFormattedDate(): String {
    val date: Long = this.toLong()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    val dateObj = Date(date)
    return dateFormat.format(dateObj)
}

fun getTimestamp(day: Int, month: Int, year: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, day)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.YEAR, year)

    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}