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
    fun loadTransaction(id: String): Transaction{
        return repository.loadTransaction(id)
    }

    private fun refreshRecentTransactions() {
        _recentTransactions.value = repository.readAllTransactionsFromDatabase()
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