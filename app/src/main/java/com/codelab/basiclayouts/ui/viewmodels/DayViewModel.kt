package com.codelab.basiclayouts.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codelab.basiclayouts.ui.components.DaySummary
import com.codelab.basiclayouts.ui.components.Transaction
import org.json.JSONObject
class DayViewModel : ViewModel(){
    private val _daySummary = MutableLiveData<DaySummary>()
    val daySummary: LiveData<DaySummary> = _daySummary

    fun loadDaySummary(){
         val jsonString = """
    {
      "year": 2023,
      "month": 8,
      "day": 10,
        "transactions": [
          {
            "title": "Groceries",
            "amount": 50,
            "category": "Food"
          },
          {
            "title": "Dinner",
            "amount": 30,
            "category": "Food"
          }
        ]
      }
    """.trimIndent()

        val jsonObject = JSONObject(jsonString)
        val jsonTransactionArray = jsonObject.getJSONArray("transactions")

        val year = jsonObject.getInt("year")
        val month = jsonObject.getInt("month")
        val day = jsonObject.getInt("day")

        val transactionList = mutableListOf<Transaction>()
        for(i in 0 until jsonTransactionArray.length()){
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
