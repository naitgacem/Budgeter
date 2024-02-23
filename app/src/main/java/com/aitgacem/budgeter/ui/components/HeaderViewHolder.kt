package com.aitgacem.budgeter.ui.components

import android.view.View
import android.widget.TextView
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.ui.toFormattedDate

class HeaderViewHolder(itemView: View) : ListViewHolder(itemView) {
    private val dayDate: TextView = itemView.findViewById(R.id.day_date)

    override fun bind(item: ItemType) {
        val header = (item as ItemType.Date)
        dayDate.text = header.date.toFormattedDate()
    }
}

