package com.codelab.basiclayouts.ui.components

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: Long ,
   val title: String,
   val amount: Int,
   val category: String = "Others"
)
