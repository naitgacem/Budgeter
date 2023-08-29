package com.aitgacem.budgeter.ui.viewmodels

import android.graphics.Typeface
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
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.ui.components.Category
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.absoluteValue

class AnalyticsViewModel(
    private val repository: TransactionsRepository,
) : ViewModel() {
    private var allTransactions = MutableStateFlow<List<Transaction>>(emptyList())

    val categoryAndValues = MutableStateFlow<List<CategoryAndValue>>(emptyList())

    init {
        viewModelScope.launch {
            loadAllTransactions()
        }
        viewModelScope.launch {
            loadCategoriAndData()
        }
    }

    private suspend fun loadAllTransactions() {
        repository.readAllTransactionsFromDatabase().collect {
            allTransactions.value = it
        }
    }

    private suspend fun loadCategoriAndData() {
        repository.getCategoryAndValue().collect {
            categoryAndValues.value = it
        }
    }

    private suspend fun group(): List<CategoryAndValue> {
        val entries = mutableListOf<CategoryAndValue>()
        withContext(Dispatchers.Default) {
            val groups = allTransactions.value
                .filter { it.category != Category.Deposit }
                .groupBy { it.category }
            var totalSpending = 0.toFloat()
            for (group in groups.entries) {
                var sum = 0.toFloat()
                for (transaction in group.value) {
                    sum += transaction.amount.absoluteValue
                }
                totalSpending += sum
                entries.add(CategoryAndValue(group.key, sum))
            }
            for (entry in entries) {
                entry.value = entry.value / totalSpending * 100
            }
        }
        return entries
    }

    fun updateChart(chart: PieChart) {
        viewModelScope.launch(Dispatchers.Default) {
            updatePieChartWithData(chart, categoryAndValues.value)
        }
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

        ds.colors = arrayListOf(
            Color.Blue.toArgb(),
            Color.LightGray.toArgb(),
            Color.Magenta.toArgb()
        )
        ds.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
        ds.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

        ds.sliceSpace = 2f
        ds.valueTextColor = Color(0xFF000000).toArgb()
        ds.valueTextSize = 18f
        ds.valueTypeface = Typeface.DEFAULT

        chart.data = PieData(ds)
        chart.invalidate()
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Budgeter).transactionsRepository
                AnalyticsViewModel(
                    repository = repository
                )
            }
        }
    }
}

