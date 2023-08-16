package com.codelab.basiclayouts.ui.components

import androidx.annotation.StringRes
import com.codelab.basiclayouts.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Overview : Screen("home", R.string.home)
    object Settings : Screen("settings", R.string.settings)
    object Profile : Screen("profile", R.string.profile)
    object AddTransaction : Screen("add_transaction", R.string.add_transaction)
    object Search : Screen("search", R.string.search)
    object Transactions : Screen("transactions", R.string.transactions)
}