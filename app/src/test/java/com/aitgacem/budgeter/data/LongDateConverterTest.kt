package com.aitgacem.budgeter.data

import com.aitgacem.budgeter.ui.getDayMonthYearFromTimestamp
import com.aitgacem.budgeter.ui.getTimestamp
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.abs
import kotlin.random.Random

class LongDateConverterTest {
    @Test
    fun testDateFormatting() {
        for (i in 1..100) {
            val timestamp = abs(Random.Default.nextLong())
            val date: Triple<Int, Int, Int> = getDayMonthYearFromTimestamp(timestamp)
            assertThat(
                getTimestamp(
                    date.first,
                    date.second,
                    date.third
                )
            ).isWithin(24 * 3600 * 1000L).of(timestamp)
        }
        val a = getDayMonthYearFromTimestamp(43321222146L)
        assertThat(a.second).isEqualTo(4)
        assertThat(a.third).isEqualTo(1971)
    }

}