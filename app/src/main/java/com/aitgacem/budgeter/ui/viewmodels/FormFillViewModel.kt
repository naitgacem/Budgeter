package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.components.ItemType.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormFillViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {

    private var _amount = MutableStateFlow<Double>(0.0)
    val amount: StateFlow<Double> = _amount

    private var _description = MutableStateFlow<String?>("")

    private var _category = MutableStateFlow<Category?>(null)
    val category = _category

    private var date = MutableStateFlow<Long>(0)

    private var isUpdate = false
    private var oldTransaction: Transaction? = null


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
            viewModelScope.launch {
                repository.writeTransactionToDatabase(
                    Transaction(
                        id = 0,
                        title = _description.value ?: "",
                        amount = if (isDeposit) {
                            _amount.value
                        } else {
                            -1 * _amount.value
                        },
                        date = date.value,
                        time = 155,
                        category = if (isDeposit) Category.Deposit else _category.value
                            ?: Category.Others,
                    )
                )
            }
    }

    fun updateTransaction(isDeposit: Boolean, old: Transaction) {
        val transaction = Transaction(
            date = date.value,
            amount = if (isDeposit) {
                _amount.value
            } else {
                -1 * _amount.value
            },
            title = _description.value ?: "",
            category = if (isDeposit) Category.Deposit else _category.value ?: Category.Others,
            time = 0,
            id = old.id
        )
        viewModelScope.launch {
            repository.updateTransaction(transaction, old)
        }
    }
}