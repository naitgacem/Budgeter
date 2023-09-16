package com.aitgacem.budgeter.ui.components

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Spa

enum class Category : Parcelable {
    Transportation,
    Groceries,
    Travel,
    Utilities,
    Entertainment,
    Housing,
    Education,
    Healthcare,
    Others,
    Deposit;

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}

val categoryToIconMap = mapOf(
    Category.Transportation to Icons.Default.DirectionsBus,
    Category.Groceries to Icons.Default.ShoppingCart,
    Category.Travel to Icons.Default.Map,
    Category.Utilities to Icons.Default.Phone,
    Category.Entertainment to Icons.Default.Movie,
    Category.Housing to Icons.Default.Apartment,
    Category.Education to Icons.Default.School,
    Category.Healthcare to Icons.Default.Spa,
    Category.Others to Icons.Default.Edit,
)
