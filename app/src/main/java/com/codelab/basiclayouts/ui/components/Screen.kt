package com.codelab.basiclayouts.ui.components

import androidx.annotation.StringRes
import com.codelab.basiclayouts.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val isNavBarVisible: Boolean = true) {

    object Overview : Screen("overview", R.string.overview)
    object Settings : Screen("settings", R.string.settings, false)
    object Profile : Screen("profile", R.string.profile)
    object Withdraw : Screen("add_transaction", R.string.add_transaction, false)
    object Search : Screen("search", R.string.search)
    object Transactions : Screen("transactions", R.string.transactions)
}

val navBarVisibleIn: Map<String, Boolean> = Screen::class.sealedSubclasses
    .mapNotNull { it.objectInstance }
    .associate { screen -> screen.route to screen.isNavBarVisible }
