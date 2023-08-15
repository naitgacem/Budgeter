package com.codelab.basiclayouts.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.codelab.basiclayouts.Budgeter
import com.codelab.basiclayouts.data.TransactionsRepository
import com.codelab.basiclayouts.data.model.DaySummary
import com.codelab.basiclayouts.data.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DataViewModel(
    private val repository: TransactionsRepository
) : ViewModel() {
    private var _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions = _recentTransactions

    init {
        refreshRecentTransactions()
        repository.transactionAddedEvent.observeForever{
            refreshRecentTransactions()
        }
    }

    private fun refreshRecentTransactions() {
        _recentTransactions.value = emptyList()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, +1)

        for (i in 0..6) {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)

            val day: DaySummary? = loadDayTransaction(formattedDate)
            if (day != null) {
                _recentTransactions.value = _recentTransactions.value + day.spending
            }
        }
    }

    private fun loadDayTransaction(date: String): DaySummary? {
        return repository.readDailyTransactionsFromFile(date)
    }

    fun saveDailyTransactions(dailyTransactions: DaySummary?) {
        repository.writeDailyTransactionsToFile(dailyTransactions)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as Budgeter).transactionsRepository
                DataViewModel(
                    repository = myRepository
                )
            }
        }
    }


}