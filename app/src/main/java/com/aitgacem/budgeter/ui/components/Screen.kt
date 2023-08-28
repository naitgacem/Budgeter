package com.aitgacem.budgeter.ui.components

import androidx.annotation.StringRes
import com.aitgacem.budgeter.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val isNavBarVisible: Boolean = true,
) {

    object Overview : Screen("overview", R.string.overview)
    object Settings : Screen("settings", R.string.settings, false)
    object Goals : Screen("goals", R.string.goals)
    object Withdraw : Screen("withdraw", R.string.add_transaction, false)
    object Deposit : Screen("deposit", R.string.deposit, false)
    object Analytics : Screen("analytics", R.string.analytics)
    object Transactions : Screen("transactions", R.string.transactions)
    object TransactionDetails :
        Screen("transactions_details/{id}", R.string.transactions_details, false)
}

val navBarVisibleIn: Map<String, Boolean> = Screen::class.sealedSubclasses
    .mapNotNull { it.objectInstance }
    .associate { screen -> screen.route to screen.isNavBarVisible }
