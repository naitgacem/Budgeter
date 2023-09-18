package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.aitgacem.budgeter.Budgeter
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

    val categories = Category.values().filter { it != Category.Deposit }

    private var _amount = MutableStateFlow<String?>(null)
    val amount: StateFlow<String?> = _amount

    private var _description = MutableStateFlow<String?>("")
    val description: StateFlow<String?> = _description

    private var _category = MutableStateFlow<Category?>(null)
    val category = _category

    private var date = MutableStateFlow<Long>(0)

    private var isUpdate = false
    private lateinit var oldTransaction: Transaction
    fun setUpUpdate(transaction: Transaction?) {
        if (transaction != null) {

            _amount.value = transaction.amount.toString()
            _description.value = transaction.title
            _category.value = transaction.category
            date.value = transaction.date
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

    fun updateId(timestamp: Long?) {
        date.value = timestamp ?: 0
    }

    fun updateCategory(category: Category) {
        _category.value = category
    }

    fun saveTransaction() {
        if (isUpdate) {
            viewModelScope.launch {
                repository.updateTransaction(
                    oldTransaction.copy(
                        date = date.value,
                        amount = _amount.value?.toFloatOrNull() ?: 0.0f,
                        title = _description.value ?: "",
                        category = _category.value ?: Category.Others
                    ),
                    oldTransaction
                )
            }
        } else {
            val transaction = Transaction(
                date = date.value,
                amount = _amount.value?.toFloatOrNull() ?: 0.0f,
                title = _description.value ?: "",
                category = _category.value ?: Category.Others,
                id = Calendar.getInstance().timeInMillis
            )
            viewModelScope.launch {
                repository.writeTransactionToDatabase(transaction)
            }
        }
    }
}