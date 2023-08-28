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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class WithdrawViewModel(
    private val repository: TransactionsRepository,
) : ViewModel() {

    val categories = Category.values()

    private var _amount = MutableStateFlow<Int?>(null)
    val amount: StateFlow<Int?> = _amount

    private var _description = MutableStateFlow<String?>("")
    val description: StateFlow<String?> = _description

    private var _category = MutableStateFlow<Category?>(null)
    val category = _category

    private var date = MutableStateFlow<Long>(0)


    fun updateAmount(newAmount: String) {
        _amount.value = newAmount.toIntOrNull()
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
        val transaction = Transaction(
            date = date.value,
            amount = _amount.value?.let { value -> -1 * value } ?: 0,
            title = _description.value ?: "",
            category = _category.value ?: Category.Others,
            id = Calendar.getInstance().timeInMillis
        )
        viewModelScope.launch {
            repository.writeTransactionToDatabase(transaction)
        }

    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Budgeter).transactionsRepository
                WithdrawViewModel(
                    repository = myRepository
                )
            }
        }
    }


}