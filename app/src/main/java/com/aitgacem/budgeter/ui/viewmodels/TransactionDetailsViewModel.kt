package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.ui.components.ItemType.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailsViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {

    private var _transaction = MutableStateFlow<Transaction?>(null)
    val transaction = _transaction

    fun loadTransaction(id: Long) {
        viewModelScope.launch {
//            val loadedTransaction = repository.loadTransaction(id)
//            _transaction.value = loadedTransaction
        }
    }
}