package com.codelab.basiclayouts.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.codelab.basiclayouts.Budgeter
import com.codelab.basiclayouts.data.TransactionsRepository
import com.codelab.basiclayouts.data.model.Transaction
import com.codelab.basiclayouts.ui.components.categoryToIconMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

class WithdrawViewModel(
    private val repository: TransactionsRepository
) : ViewModel() {

    val categories = categoryToIconMap.keys.toList()

    private var _amount = MutableStateFlow<Int?>(null)
    val amount: StateFlow<Int?> = _amount

    private var _description = MutableStateFlow<String?>("")
    val description: StateFlow<String?> = _description

    private var _category = MutableStateFlow<String>("")
    val category: StateFlow<String> = _category

    private var date = MutableStateFlow<Long>(0)


    fun updateAmount(newAmount: String){
        _amount.value = newAmount.toIntOrNull()
    }
    fun updateDescription(description: String){
        _description.value = description
    }
    fun updateId(timestamp: Long?){
        date.value = timestamp ?: 0
    }
    fun updateCategory(category: String){
        _category.value = category
    }


    fun saveTransaction(){
        val transaction = Transaction(
            date = date.value,
            amount = _amount.value?.let { value -> -1 * value } ?: 0,
            title = _description.value ?: "",
            category = _category.value,
            id = Calendar.getInstance().timeInMillis
        )
        repository.writeTransactionToDatabase(transaction)

    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Budgeter).transactionsRepository
                WithdrawViewModel(
                    repository = myRepository
                )
            }
        }
    }


}