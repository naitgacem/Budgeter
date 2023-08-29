package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.aitgacem.budgeter.Budgeter
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.data.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class OverviewViewModel(
    private val repository: TransactionsRepository,
) : ViewModel() {
    private var _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions = _recentTransactions

    private var _balance = MutableStateFlow<Float>(0.toFloat())
    val balance = _balance

    init {
        refreshRecentTransactions()
        refreshBalance()
    }

    private fun refreshRecentTransactions() {
        viewModelScope.launch {
            repository.readRecentTransactionsFromDatabase().collect { transactions ->
                _recentTransactions.value = transactions
            }
        }
    }

    private fun refreshBalance() {
        viewModelScope.launch {
            repository.readBalance().collect {
                _balance.value = it ?: 0.toFloat()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as Budgeter).transactionsRepository
                OverviewViewModel(
                    repository = myRepository
                )
            }
        }
    }
}