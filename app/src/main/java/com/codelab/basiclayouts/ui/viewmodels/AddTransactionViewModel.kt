package com.codelab.basiclayouts.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.codelab.basiclayouts.Budgeter
import com.codelab.basiclayouts.data.model.TransactionsRepository
import com.codelab.basiclayouts.ui.components.DaySummary
import com.codelab.basiclayouts.ui.components.Transaction
import com.codelab.basiclayouts.ui.components.categoryToIconMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

class AddTransactionViewModel(
    private val repository: TransactionsRepository
) : ViewModel() {

    val menuItems = categoryToIconMap.keys.toList()

    private var _amount = MutableStateFlow<Int?>(0)
    val amount: StateFlow<Int?> = _amount

    private var _description = MutableStateFlow<String?>("")
    val description: StateFlow<String?> = _description

    private var _category = MutableStateFlow<String>("")
    val category: StateFlow<String> = _category

    private var _dropDownExpanded = MutableStateFlow(false)
    val dropDownExpanded: StateFlow<Boolean> = _dropDownExpanded


    fun updateAmount(newAmount: String){
        _amount.value = newAmount.toIntOrNull()
    }
    fun updateDescription(description: String){
        _description.value = description
    }

    fun updateCategory(category: String){
        _category.value = category
    }
    fun expandOrCollapse(expanded: Boolean){
        _dropDownExpanded.value = expanded
    }

    fun saveTransaction(){
        val transaction = Transaction(
            Calendar.getInstance().timeInMillis,
            amount = _amount.value ?: 0,
            title = _description.value ?: "",
            category = _category.value
        )

        val day = DaySummary(
            year = 2023,
            month = 8,
            day = 15,
            spending = listOf(transaction),
            date = "2023-08-15"
        )
        repository.addEntry(day)

    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Budgeter).transactionsRepository
                AddTransactionViewModel(
                    repository = myRepository
                )
            }
        }
    }


}