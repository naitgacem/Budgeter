package com.aitgacem.budgeter.ui.components

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import com.aitgacem.budgeter.R

class TransactionViewHolder(itemView: View, private val onClick: (ItemType.Transaction) -> Unit) :
    ListViewHolder(itemView) {
    private val transactionTitle: TextView = itemView.findViewById(R.id.transaction_title)
    private val transactionAmount: TextView = itemView.findViewById(R.id.transaction_amount)
    private val transactionTime: TextView = itemView.findViewById(R.id.transaction_time)
    private val transactionIcon: ImageView = itemView.findViewById(R.id.transaction_icon)
    private var current: ItemType.Transaction? = null

    init {
        itemView.setOnClickListener {
            current?.let {
                onClick(it)
            }
        }
    }

    override fun bind(item: ItemType) {
        val transaction = item as ItemType.Transaction
        current = transaction
        transactionTitle.text = transaction.title
        transactionAmount.text = transaction.amount.toString()
        transactionTime.text = "09:42"
        val context = transactionTitle.context
        transactionIcon.setImageDrawable(
            AppCompatResources.getDrawable(context, transaction.category.toIcon())
        )
    }
}