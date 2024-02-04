package com.aitgacem.budgeter.data

import org.junit.Test
import com.google.common.truth.Truth.assertThat
import java.text.SimpleDateFormat
import java.util.Date

class LongDateConverterTest {
    @Test
    fun `test date formatting`() {
        val timestamp = "1707001200000"
        val dateStr = "04/02/2024"
        assertThat(longToDateStr(timestamp)).isEqualTo(dateStr)
    }

    private fun longToDateStr(timestamp: String): String {
        val date: Long = timestamp.toLong()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val dateObj = Date(date)
        return dateFormat.format(dateObj)
    }
}