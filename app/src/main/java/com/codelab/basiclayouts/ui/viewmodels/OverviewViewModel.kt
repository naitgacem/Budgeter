package com.codelab.basiclayouts.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.codelab.basiclayouts.Budgeter
import com.codelab.basiclayouts.data.TransactionsRepository
import com.codelab.basiclayouts.data.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow


class OverviewViewModel(
    private val repository: TransactionsRepository,
) : ViewModel() {
    private var _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions = _recentTransactions

    private var _balance = MutableStateFlow(0)
    val balance = _balance

    init {
        refreshRecentTransactions()
        refreshBalance()
        repository.transactionAddedEvent.observeForever {
            refreshRecentTransactions()
        }
        repository.balanceUpdatedEvent.observeForever {
            refreshBalance()
        }
    }

    private fun refreshRecentTransactions() {
        _recentTransactions.value = repository.readRecentTransactionsFromDatabase()
    }

    private fun refreshBalance() {
        _balance.value = repository.readBalance()
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