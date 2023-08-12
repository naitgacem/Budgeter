package com.codelab.basiclayouts.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codelab.basiclayouts.R
import com.codelab.basiclayouts.ui.components.DaySummary
import com.codelab.basiclayouts.ui.components.Transaction
import org.json.JSONObject

class DayViewModel(application: Application) : AndroidViewModel(application) {

    private val _daySummary = MutableLiveData<DaySummary>()
    val daySummary: LiveData<DaySummary> = _daySummary

    private val _isEmpty = mutableStateOf(false)
    private val isEmpty = _isEmpty

    private lateinit var recentDays: List<DaySummary>

    init {
        loadDaySummary()
    }

    private fun loadDaySummary() {
        val jsonString = getApplication<Application>().resources.openRawResource(R.raw.transactions)
            .bufferedReader().use {
            it.readText()
        }
        val jsonObject = JSONObject(jsonString)
        val jsonTransactionArray = jsonObject.getJSONArray("transactions")

        val year = jsonObject.getInt("year")
        val month = jsonObject.getInt("month")
        val day = jsonObject.getInt("day")

        val transactionList = mutableListOf<Transaction>()
        for (i in 0 until jsonTransactionArray.length()) {
            val transactionObject = jsonTransactionArray.getJSONObject(i)

            val title = transactionObject.getString("title")
            val amount = transactionObject.getInt("amount")
            val category = transactionObject.getString("category")

            val transaction = Transaction(title = title, amount = amount, category = category)
            transactionList.add(transaction)
        }
        _daySummary.value = DaySummary(year, month, day, transactionList)
    }


}
