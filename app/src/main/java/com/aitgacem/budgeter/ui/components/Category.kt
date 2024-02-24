package com.aitgacem.budgeter.ui.components

import android.content.Context
import android.os.Parcelable
import com.aitgacem.budgeter.R
import kotlinx.parcelize.Parcelize


@Parcelize
enum class Category : Parcelable {
    Transportation,
    Groceries,
    Food,
    Travel,
    Utilities,
    Entertainment,
    Housing,
    Education,
    Healthcare,
    Others,
    Deposit
}

fun Category.toIcon(): Int {
    return when (this) {
        Category.Transportation -> R.drawable.ic_directions_bus
        Category.Groceries -> R.drawable.ic_shopping_cart
        Category.Travel -> R.drawable.ic_map
        Category.Utilities -> R.drawable.ic_call
        Category.Entertainment -> R.drawable.ic_movie
        Category.Housing -> R.drawable.ic_apartment
        Category.Education -> R.drawable.ic_school
        Category.Healthcare -> R.drawable.ic_spa
        Category.Food -> R.drawable.ic_restaurant
        Category.Others -> R.drawable.ic_edit_note
        Category.Deposit -> R.drawable.ic_arrow_upward
    }
}

fun Category.localName(context: Context): String {
    val getString = { it: Int ->
        context.getString(it)
    }
    return when (this) {
        Category.Transportation -> getString(R.string.category_transportation)
        Category.Groceries -> getString(R.string.category_groceries)
        Category.Food -> getString(R.string.category_food)
        Category.Travel -> getString(R.string.category_travel)
        Category.Utilities -> getString(R.string.category_utilities)
        Category.Entertainment -> getString(R.string.category_entertainment)
        Category.Housing -> getString(R.string.category_housing)
        Category.Education -> getString(R.string.category_education)
        Category.Healthcare -> getString(R.string.category_healthcare)
        Category.Others -> getString(R.string.category_others)
        Category.Deposit -> getString(R.string.category_deposit)
    }
}