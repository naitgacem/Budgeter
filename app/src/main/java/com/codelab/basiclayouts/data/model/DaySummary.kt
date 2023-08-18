package com.codelab.basiclayouts.data.model

data class DaySummary(
    val year: Int, val month: Int, val day: Int,
    val spending: List<Transaction>,
    val date: String
)