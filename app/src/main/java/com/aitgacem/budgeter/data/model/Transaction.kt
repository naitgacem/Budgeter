package com.aitgacem.budgeter.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aitgacem.budgeter.ui.components.Category

@Entity
data class Transaction(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "amount") val amount: Float,
    @ColumnInfo(name = "category") val category: Category,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readParcelable(Category::class.java.classLoader) ?: Category.Transportation
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(date)
        parcel.writeString(title)
        parcel.writeFloat(amount)
        parcel.writeParcelable(category, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}