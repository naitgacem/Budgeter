package com.codelab.basiclayouts.ui.components

import kotlinx.serialization.Serializable

@Serializable
data class DaySummary(
    val year: Int, val month: Int, val day: Int,
    val spending: List<Transaction>,
    val date: String = "1970-01-01"
)