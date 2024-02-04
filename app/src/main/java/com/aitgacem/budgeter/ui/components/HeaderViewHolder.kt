package com.aitgacem.budgeter.ui.components

import android.view.View
import android.widget.TextView
import com.aitgacem.budgeter.R
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

class HeaderViewHolder(itemView: View) : ListViewHolder(itemView) {
    private val dayDate: TextView = itemView.findViewById(R.id.day_date)

    override fun bind(item: ItemType) {
        val header = (item as ItemType.Date)
        dayDate.text = longToDateStr(header.date)
    }

    private fun longToDateStr(timestamp: String): String {
        val date: Long = timestamp.toLong()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val dateObj = Date(date)
        return dateFormat.format(dateObj)
    }
}