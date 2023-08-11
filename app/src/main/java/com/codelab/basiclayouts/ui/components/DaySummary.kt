package com.codelab.basiclayouts.ui.components

data class DaySummary(
    val year: Int, val month: Int, val day: Int,
    val spending: List<Transaction>
)