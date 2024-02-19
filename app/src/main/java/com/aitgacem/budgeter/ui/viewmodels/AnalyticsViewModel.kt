package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.data.model.CategoryAndValue
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.getCurrentMonthStart
import com.aitgacem.budgeter.ui.oneMonthLater
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {

    private var chartViewType = ChartViewType.MONTH_VIEW
    private val curMonthStart: Long = getCurrentMonthStart()
    private val curMonthEnd = curMonthStart.oneMonthLater()
    val curMonthBalanceData = repository.getDailyBalance(curMonthStart, curMonthEnd)
    val curMonthSpendingData: LiveData<List<CategoryAndValue>> = repository.getSpending()

}

private enum class ChartViewType {
    MONTH_VIEW,
    YEAR_VIEW,
}



