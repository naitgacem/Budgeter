package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.ui.components.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    private lateinit var oldTransaction: Transaction


    suspend fun setUpUpdate(transaction: Transaction?) {
        if (transaction != null) {
            oldTransaction = transaction
            _amount.value = transaction.amount.toString()
            _description.value = transaction.title
            _date.value = transaction.date
            oldTransaction = transaction
            isUpdate = true
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
            viewModelScope.launch {
                repository.updateTransaction(
                    oldTransaction.copy(
                        date = _date.value,
                        amount = _amount.value?.toFloatOrNull() ?: 0.0f,
                        title = _description.value ?: "",
                    ),
                    oldTransaction,
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