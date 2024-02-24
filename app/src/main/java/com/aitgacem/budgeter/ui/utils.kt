package com.aitgacem.budgeter.ui

import android.content.Context
import com.aitgacem.budgeter.R
import java.text.DateFormat.getDateInstance
import java.util.Calendar
import java.util.Date

fun String.toFormattedDate(): String {
    val date: Long = this.toLong()
    val a = getDateInstance()
    val dateObj = Date(date)
    return a.format(dateObj)
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

fun Int?.toMonthStr(context: Context, short: Boolean = false): String {
    val getString = { it: Int ->
        context.getString(it)
    }
    return when (this) {
        0 -> if (short) getString(R.string.january_short) else getString(R.string.january_long)
        1 -> if (short) getString(R.string.february_short) else getString(R.string.february_long)
        2 -> if (short) getString(R.string.march_short) else getString(R.string.march_long)
        3 -> if (short) getString(R.string.april_short) else getString(R.string.april_long)
        4 -> if (short) getString(R.string.may_short) else getString(R.string.may_long)
        5 -> if (short) getString(R.string.june_short) else getString(R.string.june_long)
        6 -> if (short) getString(R.string.july_short) else getString(R.string.july_long)
        7 -> if (short) getString(R.string.august_short) else getString(R.string.august_long)
        8 -> if (short) getString(R.string.september_short) else getString(R.string.september_long)
        9 -> if (short) getString(R.string.october_short) else getString(R.string.october_long)
        10 -> if (short) getString(R.string.november_short) else getString(R.string.november_long)
        11 -> if (short) getString(R.string.december_short) else getString(R.string.december_long)
        else -> ""
    }
}
