package com.aitgacem.budgeter.ui.components

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.data.model.Transaction

class TransactionViewHolder(itemView: View, private val onClick: (Transaction) -> Unit) :
    ListViewHolder(itemView) {
    private val transactionTitle: TextView = itemView.findViewById(R.id.transaction_title)
    private val transactionAmount: TextView = itemView.findViewById(R.id.transaction_amount)
    private val transactionTime: TextView = itemView.findViewById(R.id.transaction_time)
    private val transactionIcon: ImageView = itemView.findViewById(R.id.transaction_icon)


    override fun bind(item: ItemType) {
        val transaction = item as ItemType.Transaction
        transactionTitle.text = transaction.title
        transactionAmount.text = transaction.amount.toString()
        transactionTime.text = "09:42"
        val context = transactionTitle.context
        transactionIcon.setImageDrawable(
            ContextCompat.getDrawable(
                context.applicationContext, transaction.category.toIcon()
            )
        )
    }
}