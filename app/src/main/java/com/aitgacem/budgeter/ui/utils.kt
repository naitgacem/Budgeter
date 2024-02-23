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

fun getCurrentYearStart(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.MONTH, 0)
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

fun Long.oneYearLater(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.add(Calendar.YEAR, 1)
    return calendar.timeInMillis
}

fun Long.oneYearEarlier(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.add(Calendar.YEAR, -1)
    return calendar.timeInMillis
}

fun Long.oneMonthEarlier(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.add(Calendar.MONTH, -1)
    return calendar.timeInMillis
}


fun mapToList(map: Map<Long, Double>): List<Pair<Int, Double>> {
    return map.entries.map {
        it.key.getDayOfMonth() to it.value
    }
}

fun mapYear(map: Map<Long, Double>): List<Pair<Int, Double>> {
    val calendar = Calendar.getInstance()
    return map.entries
        .groupBy { entry ->
            calendar.apply {
                timeInMillis = entry.key
            }
            calendar.get(Calendar.MONTH)
        }
        .mapValues { (_, entries) ->
            entries.maxByOrNull { it.key }?.value ?: 0.0
        }
        .toList()
}

fun Long.getDayOfMonth(): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun Int?.toMonthStr(short: Boolean = false): String {
    return when (this) {
        0 -> if (short) "Jan" else "January"
        1 -> if (short) "Feb" else "February"
        2 -> if (short) "Mar" else "March"
        3 -> if (short) "Apr" else "April"
        4 -> if (short) "May" else "May"
        5 -> if (short) "Jun" else "June"
        6 -> if (short) "Jul" else "July"
        7 -> if (short) "Aug" else "August"
        8 -> if (short) "Sep" else "September"
        9 -> if (short) "Oct" else "October"
        10 -> if (short) "Nov" else "November"
        11 -> if (short) "Dec" else "December"
        else -> ""
    }
}
