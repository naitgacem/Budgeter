package com.aitgacem.budgeter.ui.components

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
        Category.Transportation -> R.drawable.ic_apartment
        Category.Groceries -> R.drawable.ic_apartment
        Category.Travel -> R.drawable.ic_map
        Category.Utilities -> R.drawable.ic_movie
        Category.Entertainment -> R.drawable.ic_movie
        Category.Housing -> R.drawable.ic_apartment
        Category.Education -> R.drawable.ic_school
        Category.Healthcare -> R.drawable.ic_spa
        Category.Food -> R.drawable.ic_restaurant
        Category.Others -> R.drawable.ic_movie
        Category.Deposit -> R.drawable.ic_arrow_upward
    }
}