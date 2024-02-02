package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.data.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {

    private var _allTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val allTransactions = _allTransactions.asStateFlow()
    val transactionsLiveData = _allTransactions.asLiveData()
    init {
        viewModelScope.launch {
            repository.readAllTransactionsFromDatabase().collect { transactions ->
                _allTransactions.value = transactions
            }
        }
    }

}