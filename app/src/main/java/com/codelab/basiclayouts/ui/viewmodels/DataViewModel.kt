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


class DataViewModel(
    private val repository: TransactionsRepository,
) : ViewModel() {
    private var _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions = _recentTransactions

    private var _allTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val allTransactions = _allTransactions

    private var _balance = MutableStateFlow(0)
    val balance = _balance

    init {
        refreshRecentTransactions()
        loadAllTransactions()
        refreshBalance()
        repository.transactionAddedEvent.observeForever {
            refreshRecentTransactions()
            loadAllTransactions()
        }
        repository.balanceUpdatedEvent.observeForever {
            refreshBalance()
        }
    }

    fun loadTransaction(id: Long): Transaction {
        return repository.loadTransaction(id)
    }

    private fun refreshRecentTransactions() {
        _recentTransactions.value = repository.readRecentTransactionsFromDatabase()
    }

    private fun refreshBalance() {
        _balance.value = repository.readBalance()
    }

    private fun loadAllTransactions() {
        _allTransactions.value = repository.readAllTransactionsFromDatabase()
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