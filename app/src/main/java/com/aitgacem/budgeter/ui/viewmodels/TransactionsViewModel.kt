package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.aitgacem.budgeter.data.TransactionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {
    private var _filter = MutableLiveData("")

    val filteredList = _filter.switchMap { filter ->
        repository.getDayAndTransactions(filter)
    }

    fun updateFilter(filter: String) {
        _filter.value = filter
    }
}