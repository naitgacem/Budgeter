package com.aitgacem.budgeter.ui.viewmodels.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date

class DateFormatter : ValueFormatter() {
    override fun getFormattedValue(
        value: Float,
    ): String {
        val dateInMillis = value.toLong()

        return SimpleDateFormat("dd/MM").format(Date(dateInMillis))
    }
}