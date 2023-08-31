package com.aitgacem.budgeter.ui.viewmodels

import android.graphics.Typeface
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.aitgacem.budgeter.Budgeter
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.data.model.CategoryAndValue
import com.aitgacem.budgeter.data.model.DateAndBalance
import com.aitgacem.budgeter.data.model.Transaction
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {
    private var allTransactions = MutableStateFlow<List<Transaction>>(emptyList())

    private val categoryAndValues = MutableStateFlow<List<CategoryAndValue>>(emptyList())

    private val dateAndBalance = MutableStateFlow<List<DateAndBalance>>(emptyList())

    init {
        viewModelScope.launch {
            loadAllTransactions()
        }
        viewModelScope.launch {
            loadCategoryAndData()
        }
        viewModelScope.launch {
            loadDayAndData()
        }

    }

    private suspend fun loadAllTransactions() {
        repository.readAllTransactionsFromDatabase().collect {
            allTransactions.value = it
        }
    }

    private suspend fun loadDayAndData() {
        repository.getBalanceByDate().collect {
            dateAndBalance.value = it
            var s = ""
            for (date in dateAndBalance.value) {
                s =
                    s + SimpleDateFormat("dd/MM/yyyy").format(Date(date.date)) + " : " + date.amount.toString() + "\n"
            }
            Log.d("sss", s)
        }
    }

    private suspend fun loadCategoryAndData() {
        repository.getCategoryAndValue().collect {
            categoryAndValues.value = it
        }
    }

    fun updatePieChart(chart: PieChart) {
        updatePieChartWithData(chart, categoryAndValues.value)
    }

    private fun updatePieChartWithData(
        chart: PieChart,
        data: List<CategoryAndValue>,
    ) {
        val entries = ArrayList<PieEntry>()
        for (item in data) {
            entries.add(
                PieEntry(item.value, item.category.name)
            )
        }

        val ds = PieDataSet(entries, "")
        setPieChartProperties(ds)

        ds.valueFormatter = PercentFormatter(chart)
        chart.data = PieData(ds)
        chart.invalidate()
    }

    fun updateLineChart(
        chart: LineChart,
    ) {
        updateLineChartWithData(chart, dateAndBalance.value)
    }

    private fun updateLineChartWithData(
        chart: LineChart,
        data: List<DateAndBalance>,
    ) {
        val entries = ArrayList<Entry>()
        for (item in data) {
            entries.add(
                Entry(item.date.toFloat(), item.amount)
            )
        }

        val ds = LineDataSet(entries, "")
        setLineChartProperties(ds)

        chart.data = LineData(ds)
        chart.fitScreen()
        chart.setVisibleXRange(0f, (7 * 24 * 3600 * 1000).toFloat()) // between 0 and 10 days
        chart.moveViewToX(chart.xChartMax)
    }

    private fun setPieChartProperties(ds: PieDataSet) {
        ds.apply {
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            valueLinePart1OffsetPercentage = 100f
            valueLinePart1Length = 0.6f
            valueLinePart2Length = 0.4f
            sliceSpace = 2f
            valueTextColor = Color(0xFF000000).toArgb()
            valueTextSize = 18f
            valueTypeface = Typeface.DEFAULT
            colors = com.aitgacem.budgeter.ui.components.colors.map {
                it.toArgb()
            }
        }
    }

    private fun setLineChartProperties(ds: LineDataSet) {
        ds.apply {
            valueTextColor = Color(0xFF000000).toArgb()
            valueTextSize = 18f
            valueTypeface = Typeface.DEFAULT
            setDrawValues(false)
            colors = listOf(
                Color.Black.toArgb(),
            )
            circleColors = listOf(Color.Blue.toArgb())
        }
    }

}


class MyCustomFormatter(
) : ValueFormatter() {
    override fun getFormattedValue(
        value: Float,
    ): String {
        val dateInMillis = value.toLong()

        return SimpleDateFormat("dd/MM").format(Date(dateInMillis))
    }
}
