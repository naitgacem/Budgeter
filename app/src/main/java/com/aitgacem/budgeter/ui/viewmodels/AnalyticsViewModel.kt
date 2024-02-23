package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.data.model.CategoryAndValue
import com.aitgacem.budgeter.ui.getCurrentMonthStart
import com.aitgacem.budgeter.ui.getCurrentYearStart
import com.aitgacem.budgeter.ui.getDayMonthYearFromTimestamp
import com.aitgacem.budgeter.ui.oneMonthEarlier
import com.aitgacem.budgeter.ui.oneMonthLater
import com.aitgacem.budgeter.ui.oneYearEarlier
import com.aitgacem.budgeter.ui.oneYearLater
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {

    private var _chartViewType = MutableLiveData(ChartViewType.MONTH_VIEW)
    val chartViewType: LiveData<ChartViewType> = _chartViewType

    private var curMonthStart: Long = getCurrentMonthStart()
    private var curYearStart: Long = getCurrentYearStart()

    private val _curMonth = MutableLiveData(getDayMonthYearFromTimestamp(curMonthStart).second)
    val curMonth: LiveData<Int> = _curMonth

    private val _curYear = MutableLiveData(getDayMonthYearFromTimestamp(curMonthStart).third)
    val curYear: LiveData<Int> = _curYear


    var curMonthBalanceData: LiveData<Map<Long, Double>> = _curMonth.switchMap {
        if (_chartViewType.value == ChartViewType.MONTH_VIEW)
            repository.getDailyBalance(curMonthStart, curMonthStart.oneMonthLater())
        else
            repository.getDailyBalance(curMonthStart, curMonthStart.oneYearLater())
    }

    var curMonthSpendingData: LiveData<List<CategoryAndValue>> = _curMonth.switchMap {
        repository.getSpending(curMonth.value ?: 0, _curYear.value ?: 0)
    }


    fun moveForward() {
        curMonthStart = if (_chartViewType.value == ChartViewType.MONTH_VIEW) {
            curMonthStart.oneMonthLater()
        } else {
            curMonthStart.oneYearLater()
        }
        _curYear.value = getDayMonthYearFromTimestamp(curMonthStart).third
        _curMonth.value = getDayMonthYearFromTimestamp(curMonthStart).second
    }

    fun moveBackward() {
        curMonthStart = if (_chartViewType.value == ChartViewType.MONTH_VIEW) {
            curMonthStart.oneMonthEarlier()
        } else {
            curMonthStart.oneYearEarlier()
        }
        _curYear.value = getDayMonthYearFromTimestamp(curMonthStart).third
        _curMonth.value = getDayMonthYearFromTimestamp(curMonthStart).second
    }

    fun switchViewType(type: ChartViewType) {
        if (type == ChartViewType.MONTH_VIEW) {
            _chartViewType.value = ChartViewType.MONTH_VIEW
        } else {
            _chartViewType.value = ChartViewType.YEAR_VIEW
        }

        refreshData()
    }

    private fun refreshData() {
        _curMonth.value = -1
        curYearStart = getCurrentYearStart()
        curMonthStart = getCurrentMonthStart()
        _curYear.value = getDayMonthYearFromTimestamp(curMonthStart).third
        _curMonth.value = getDayMonthYearFromTimestamp(curMonthStart).second
    }
}

enum class ChartViewType {
    MONTH_VIEW,
    YEAR_VIEW,
}



