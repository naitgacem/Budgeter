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


class TransactionsViewModel(
    private val repository: TransactionsRepository,
) : ViewModel() {

    private var _allTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val allTransactions = _allTransactions

    init {
        loadAllTransactions()
        repository.transactionAddedEvent.observeForever {
            loadAllTransactions()
        }
    }

    private fun loadAllTransactions() {
        _allTransactions.value = repository.readAllTransactionsFromDatabase()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as Budgeter).transactionsRepository
                TransactionsViewModel(
                    repository = myRepository
                )
            }
        }
    }
}