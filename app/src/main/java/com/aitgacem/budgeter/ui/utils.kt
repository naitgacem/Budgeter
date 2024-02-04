package com.aitgacem.budgeter.ui.components

import java.text.SimpleDateFormat
import java.util.Date

fun String.toFormattedDate(): String {
    val date: Long = this.toLong()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    val dateObj = Date(date)
    return dateFormat.format(dateObj)
}