package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.data.model.CategoryAndValue
import com.aitgacem.budgeter.ui.getCurrentMonthStart
import com.aitgacem.budgeter.ui.getDayMonthYearFromTimestamp
import com.aitgacem.budgeter.ui.oneMonthEarlier
import com.aitgacem.budgeter.ui.oneMonthLater
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {

    private var chartViewType = ChartViewType.MONTH_VIEW
    private var curMonthStart: Long = getCurrentMonthStart()

    private val _curMonth = MutableLiveData(getDayMonthYearFromTimestamp(curMonthStart).second)
    val curMonth: LiveData<Int> = _curMonth

    private val _curYear = MutableLiveData(getDayMonthYearFromTimestamp(curMonthStart).third)
    val curYear: LiveData<Int> = _curYear


    var curMonthBalanceData = _curMonth.switchMap {
        repository.getDailyBalance(curMonthStart, curMonthStart.oneMonthLater())
    }
    var curMonthSpendingData: LiveData<List<CategoryAndValue>> = _curMonth.switchMap {
        repository.getSpending(curMonth.value ?: 0, _curYear.value ?: 0)
    }

    fun moveForward() {
        curMonthStart = curMonthStart.oneMonthLater()

        _curMonth.value = getDayMonthYearFromTimestamp(curMonthStart).second
        _curYear.value = getDayMonthYearFromTimestamp(curMonthStart).third
    }

    fun moveBackward() {
        curMonthStart = curMonthStart.oneMonthEarlier()

        _curMonth.value = getDayMonthYearFromTimestamp(curMonthStart).second
        _curYear.value = getDayMonthYearFromTimestamp(curMonthStart).third
    }
}

private enum class ChartViewType {
    MONTH_VIEW,
    YEAR_VIEW,
}



