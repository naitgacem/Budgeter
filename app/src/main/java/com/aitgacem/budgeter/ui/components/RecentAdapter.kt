package com.aitgacem.budgeter.ui.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.ui.components.ItemType.Transaction
import androidx.recyclerview.widget.DiffUtil

class RecentAdapter(private val onClick: (Transaction) -> Unit) :
    ListAdapter<Transaction, RecentAdapter.RecentVH>(TransactionDiffCallback) {

    class RecentVH(itemView: View, val onClick: (Transaction) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val transactionTitle: TextView = itemView.findViewById(R.id.transaction_title)
        private val transactionIcon: ImageView = itemView.findViewById(R.id.transaction_icon)
        private val transactionAmount: TextView = itemView.findViewById(R.id.transaction_amount)
        private var currentTransaction: Transaction? = null

        init {
            itemView.setOnClickListener {
                currentTransaction?.let {
                    onClick(it)
                }
            }
        }

        fun bind(transaction: Transaction) {
            currentTransaction = transaction

            transactionTitle.text = transaction.title
            transactionIcon.setImageDrawable(
                AppCompatResources.getDrawable(
                    itemView.context,
                    transaction.category.toIcon()
                )
            )
            transactionAmount.text = transaction.amount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_transaction, parent, false)
        return RecentVH(view, onClick)
    }

    override fun onBindViewHolder(holder: RecentVH, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)

    }
}

object TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }
}
