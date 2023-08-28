package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.aitgacem.budgeter.Budgeter
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.screens.CategoryAndValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.absoluteValue

class AnalyticsViewModel(
    private val repository: TransactionsRepository,
) : ViewModel() {
    private var _allTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val allTransactions = _allTransactions

    val categoryAndValues = combine(_allTransactions) {
        group()
    }

    init {
        viewModelScope.launch {
            loadAllTransactions()
        }
    }

    private suspend fun loadAllTransactions() {
        repository.readAllTransactionsFromDatabase().collect {
            _allTransactions.value = it
        }
    }

    private suspend fun group(): List<CategoryAndValue> {
        val entries = mutableListOf<CategoryAndValue>()
        withContext(Dispatchers.Default) {
            val groups = _allTransactions.value
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

