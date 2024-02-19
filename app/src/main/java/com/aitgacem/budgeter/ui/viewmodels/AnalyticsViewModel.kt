package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.aitgacem.budgeter.data.TransactionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {

    private var chartViewType = ChartViewType.MONTH_VIEW
    private val curMonthStart: Long = 0

}

private enum class ChartViewType {
    MONTH_VIEW,
    YEAR_VIEW,
}



