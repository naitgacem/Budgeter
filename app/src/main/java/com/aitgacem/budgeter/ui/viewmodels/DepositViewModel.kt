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

    private var date = MutableStateFlow<Long>(0)

    private var isUpdate = false
    private var oldValue: Float? = null
    private var oldId: Long = 0
    fun setUpUpdate(id: String) {
        isUpdate = true
        viewModelScope.launch {
            val transaction = repository.loadTransaction(id = id.toLong())
            oldValue = transaction?.amount
            oldId = transaction?.id ?: 0
            _amount.value = transaction?.amount.toString()
            _description.value = transaction?.title
        }
    }

    fun updateAmount(newAmount: String) {
        _amount.value = newAmount
    }

    fun updateDescription(description: String) {
        _description.value = description
    }

    fun updateId(timestamp: Long?) {
        date.value = timestamp ?: 0
    }

    fun saveTransaction() {
        val transaction = Transaction(
            date = date.value,
            amount = _amount.value?.toFloatOrNull() ?: 0.0f,
            title = _description.value ?: "",
            category = Category.Deposit,
            id = Calendar.getInstance().timeInMillis
        )
        viewModelScope.launch {
            if (isUpdate) {
                repository.updateTransaction(
                    transaction.copy(id = oldId),
                    oldValue
                )
            } else {
                repository.writeTransactionToDatabase(transaction)
            }
        }
    }
}