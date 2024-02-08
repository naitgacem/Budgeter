package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.data.model.CategoryAndValue
import com.aitgacem.budgeter.data.model.DateAndBalance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {

    private var _categoryAndValues = MutableStateFlow<List<CategoryAndValue>>(emptyList())
    val categoryAndValues = _categoryAndValues.asStateFlow()


    private var _dateAndBalance = repository.getDayAndBalance()
    val dateAndBalance = _dateAndBalance

}

