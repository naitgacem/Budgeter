package com.aitgacem.budgeter.ui.viewmodels

import android.graphics.Typeface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {
    var allTransactions = MutableStateFlow<List<Transaction>>(emptyList())

    private var _updateTrigger = MutableStateFlow<List<Any>>(emptyList())
    val updateTrigger = _updateTrigger.asStateFlow()

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
            _updateTrigger.value = _updateTrigger.value + Unit
            dateAndBalance.value = it
        }
    }

    private suspend fun loadCategoryAndData() {
        repository.getCategoryAndValue().collect {
            _updateTrigger.value = _updateTrigger.value + Unit
            categoryAndValues.value = it
        }
    }

    fun updatePieChart(chart: PieChart) {
        updatePieChartWithData(chart, categoryAndValues.value.filter { it.value > 0 })
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

