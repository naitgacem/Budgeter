package com.codelab.basiclayouts.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.codelab.basiclayouts.Budgeter
import com.codelab.basiclayouts.data.TransactionsRepository
import com.codelab.basiclayouts.data.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val repository: TransactionsRepository,
) : ViewModel() {
    private var _allTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val allTransactions = _allTransactions

    init {
        loadAllTransactions()
    }

    private fun loadAllTransactions() {
        viewModelScope.launch {
            repository.readAllTransactionsFromDatabase().collect {
                _allTransactions.value = it
            }
        }
    }

    private fun group() {

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

