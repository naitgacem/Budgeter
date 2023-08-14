package com.codelab.basiclayouts.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddTransactionViewModel : ViewModel() {
    private val ALLMENUITEMS = listOf("General", "Housing", "Education")
    private var _menuItems = MutableStateFlow<List<String>>(emptyList())
    val menuItems = _menuItems

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
        _menuItems.value = ALLMENUITEMS.filter { it.startsWith(category, ignoreCase = true) }
    }
    fun expandOrCollapse(expanded: Boolean){
        _dropDownExpanded.value = expanded
    }


}