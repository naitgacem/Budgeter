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

fun getDayMonthYearFromTimestamp(timestamp: Long): Triple<Int, Int, Int> {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp

    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    return Triple(day, month, year)
}

fun getCurrentMonthStart(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun Long.oneMonthLater(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.add(Calendar.MONTH, 1)
    return calendar.timeInMillis
}

fun mapToList(map: Map<Long, Double>): List<Pair<Int, Double>> {
    return map.entries.map {
        it.key.getDayOfMonth() to it.value
    }
}

fun Long.getDayOfMonth(): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.get(Calendar.DAY_OF_MONTH)
}
