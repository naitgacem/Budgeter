package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.aitgacem.budgeter.data.TransactionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {

    val transactionsLiveData = repository.readAllTransactionsFromDatabase()

    val transactions = repository.getDayAndTransactions()
}