package com.aitgacem.budgeter.ui.viewmodels

import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
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
class WithdrawViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {

    val categories = Category.entries.filter { it != Category.Deposit }

    private var _amount = MutableStateFlow<Double>(0.0)
    val amount: StateFlow<Double> = _amount

    private var _description = MutableStateFlow<String?>("")
    val description: StateFlow<String?> = _description

    private var _category = MutableStateFlow<Category?>(null)
    val category = _category

    private var date = MutableStateFlow<Long>(0)

    private var isUpdate = false
    private var oldTransaction: Transaction? = null

    @OptIn(ExperimentalMaterial3Api::class)
    val datePickerState = DatePickerState(
        CalendarLocale.getDefault(),
        initialDisplayMode = DisplayMode.Input,
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis
    )

    fun setUpUpdate(transaction: Transaction?) {
        if (transaction != null) {
            oldTransaction = transaction
            _amount.value = transaction.amount
            _description.value = transaction.title
            _category.value = transaction.category
            date.value = transaction.date
            isUpdate = true
        }
    }

    fun updateAmount(newAmount: String) {
        val value = newAmount.toDoubleOrNull()
        if (value != null) {
            _amount.value = value
        }
    }

    fun updateDescription(description: String) {
        _description.value = description
    }

    fun updateId(timestamp: Long?) {
        date.value = timestamp ?: 0
    }

    fun updateCategory(category: Category) {
        _category.value = category
    }

    fun saveTransaction(isDeposit: Boolean) {
        if (isUpdate) {
            viewModelScope.launch {
//                repository.updateTransaction(
//                    oldTransaction!!.copy(
//                        date = date.value,
//                        amount = _amount.value?.toFloatOrNull() ?: 0.0f,
//                        title = _description.value ?: "",
//                        category = _category.value ?: Category.Others
//                    ),
//                    oldTransaction
//                )
            }
        } else {
//            val transaction = Transaction(
//                date = date.value,
//                amount = _amount.value?.toFloatOrNull() ?: 0.0f,
//                title = _description.value ?: "",
//                category = _category.value ?: Category.Others,
//                id = Calendar.getInstance().timeInMillis
//            )
//            viewModelScope.launch {
//                repository.writeTransactionToDatabase(transaction)
//            }
            viewModelScope.launch {
                repository.writeTransactionToDatabase(
                    Transaction(
                        0, _description.value ?: "",
                        if (isDeposit) {
                            _amount.value
                        } else {
                            -1 * _amount.value
                        },
                        date.value,
                        155, _category.value ?: Category.Others,
                    )
                )
            }
        }
    }
}