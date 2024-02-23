package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.ui.components.ItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {
    private var _filter = MutableLiveData("")

    val transactions: LiveData<Map<ItemType.Date, List<ItemType.Transaction>>> =
        repository.getDayAndTransactions()

    val filteredList = _filter.switchMap { filter ->
        repository.getDayAndTransactions().map { map ->
            map.mapValues { (_, list) ->
                list.filter {
                    it.title.contains(filter.toString())
                }
            }
        }
    }

    fun updateFilter(filter: String) {
        _filter.value = filter
    }
}