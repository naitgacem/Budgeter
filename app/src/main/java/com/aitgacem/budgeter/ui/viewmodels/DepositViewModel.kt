package com.aitgacem.budgeter.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.ui.components.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {

    private var _amount = MutableStateFlow<String?>(null)
    val amount: StateFlow<String?> = _amount

    private var _description = MutableStateFlow<String?>("")
    val description: StateFlow<String?> = _description

    private var _date = MutableStateFlow<Long>(0)
    val date: StateFlow<Long> = _date

    private var isUpdate = false

    private var oldValue: Float? = null

    private var oldTransaction: Transaction? = null

    suspend fun setUpUpdate(id: String) {
        val transaction = repository.loadTransaction(id = id.toLong())
        if (transaction != null) {
            _date.value = transaction.date
            oldValue = transaction.amount
            _amount.value = transaction.amount.toString()
            _description.value = transaction.title
            oldTransaction = transaction
            isUpdate = true
            Log.d("nabil", "should finish")
        } else {
            throw IOException("Transaction deleted or corrupt")
        }
    }

    fun updateAmount(newAmount: String) {
        _amount.value = newAmount
    }

    fun updateDescription(description: String) {
        _description.value = description
    }

    fun updateDate(timestamp: Long?) {
        _date.value = timestamp ?: 0
    }

    fun saveTransaction() {
        if (isUpdate) {
            Log.d("nabil", "but it works")

            viewModelScope.launch {
                repository.updateTransaction(
                    oldTransaction!!.copy(
                        date = _date.value,
                        amount = _amount.value?.toFloatOrNull() ?: 0.0f,
                        title = _description.value ?: "",
                    ),
                    oldValue
                )

            }
        } else {
            val transaction = Transaction(
                date = _date.value,
                amount = _amount.value?.toFloatOrNull() ?: 0.0f,
                title = _description.value ?: "",
                category = Category.Deposit,
                id = Calendar.getInstance().timeInMillis
            )
            viewModelScope.launch {
                repository.writeTransactionToDatabase(transaction)
            }
        }

    }
}