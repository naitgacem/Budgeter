package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.aitgacem.budgeter.data.TransactionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {

    val balanceLiveData = repository.readLatestBalence()
}