package com.aitgacem.budgeter.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.components.ItemType.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class FormFillViewModel @Inject constructor(
    private val repository: TransactionsRepository,
) : ViewModel() {

    private var _amount = MutableLiveData<Double>(0.0)
    val amount: LiveData<Double> = _amount

    private var _description = MutableLiveData("")
    val description: LiveData<String> = _description

    private var _category = MutableLiveData<Category?>(null)
    val category: LiveData<Category?> = _category

    private var _date: MutableLiveData<Long> = MutableLiveData<Long>(0)
    val date: LiveData<Long> = _date

    fun initialize(transaction: Transaction) {
        _amount.value = transaction.amount
        _description.value = transaction.title
        _category.value = transaction.category
        _date.value = transaction.date
    }

    fun updateAmount(newAmount: String) {
        val value = newAmount.toDoubleOrNull()
        if (value != null) {
            _amount.value = abs(value)
        }
    }

    fun updateDescription(description: String) {
        _description.value = description
    }

    fun updateId(timestamp: Long?) {
        _date.value = timestamp ?: 0
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
                        amount = _amount.value ?: 0.0,
                        date = _date.value ?: Calendar.getInstance().timeInMillis,
                        time = 155,
                        category = if (isDeposit) Category.Deposit else _category.value
                            ?: Category.Others,
                    )
                )
            }
    }

    fun updateTransaction(isDeposit: Boolean, old: Transaction) {
        val transaction = Transaction(
            date = _date.value ?: Calendar.getInstance().timeInMillis,
            amount = _amount.value ?: 0.0,
            title = _description.value ?: "",
            category = if (isDeposit) Category.Deposit else _category.value ?: Category.Others,
            time = 0,
            id = old.id
        )
        viewModelScope.launch {
            repository.updateTransaction(transaction, old)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

}