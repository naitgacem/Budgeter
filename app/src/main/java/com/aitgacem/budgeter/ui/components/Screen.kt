package com.aitgacem.budgeter.ui.components

import androidx.annotation.StringRes
import com.aitgacem.budgeter.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
) {

    object Home : Screen("home", R.string.home)
    object Settings : Screen("settings", R.string.settings)
    object Goals : Screen("goals", R.string.goals)
    object Withdraw : Screen("withdraw", R.string.add_transaction)
    object Deposit : Screen("deposit?id={id}", R.string.deposit)
    object Analytics : Screen("analytics", R.string.analytics)
    object Transactions : Screen("transactions", R.string.transactions)
    object TransactionDetails : Screen("transactions_details/{id}", R.string.transactions_details)
}
