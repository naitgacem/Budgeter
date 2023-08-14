package com.codelab.basiclayouts.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddTransactionViewModel : ViewModel() {
    var _amount: StateFlow<Int> = MutableStateFlow(0)
    var amount: StateFlow<Int> = _amount


}