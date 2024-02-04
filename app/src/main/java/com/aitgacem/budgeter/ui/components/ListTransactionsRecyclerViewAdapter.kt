package com.aitgacem.budgeter.ui.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.data.model.Transaction

class TransactionAdapter(private val onClick: (Transaction) -> Unit) :
    ListAdapter<ItemType, ListViewHolder>(ListItemDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ListViewHolder.init(view, onClick)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        return holder.bind(getItem(position))

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ItemType.Item -> R.layout.item_list_transactions
            is ItemType.Date -> R.layout.item_day_divider
        }
    }

}


sealed class ItemType {
    class Date(val date: String) : ItemType()
    class Item(val transaction: Transaction) : ItemType()
}

sealed class ListViewHolder(view: View, onClick: (Transaction) -> Unit) :
    RecyclerView.ViewHolder(view) {

    abstract fun bind(item: ItemType)

    class TransactionViewHolder(itemView: View, private val onClick: (Transaction) -> Unit) :
        ListViewHolder(itemView, onClick) {
        private val transactionTitle: TextView = itemView.findViewById(R.id.transaction_title)
        private val transactionAmount: TextView = itemView.findViewById(R.id.transaction_amount)
        private val transactionTime: TextView = itemView.findViewById(R.id.transaction_time)
        private val transactionIcon: ImageView = itemView.findViewById(R.id.transaction_icon)
        private var currentTransaction: Transaction? = null

        init {
            itemView.setOnClickListener {
                currentTransaction?.let(onClick)
            }
        }

        override fun bind(item: ItemType) {
            val transaction = (item as ItemType.Item).transaction
            currentTransaction = transaction
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

    class HeaderViewHolder(itemView: View) : ListViewHolder(itemView, onClick = {}) {
        private val dayDate: TextView = itemView.findViewById(R.id.day_date)

        override fun bind(item: ItemType) {
            val header = (item as ItemType.Date)
            dayDate.text = header.date
        }
    }

    companion object {
        fun init(view: View, onClick: (Transaction) -> Unit): ListViewHolder {

            return when (view.id) {
                R.layout.item_day_divider -> HeaderViewHolder(view)
                else -> TransactionViewHolder(view, onClick)
            }
        }
    }
}

object ListItemDiffCallback : DiffUtil.ItemCallback<ItemType>() {
    override fun areItemsTheSame(oldItem: ItemType, newItem: ItemType): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ItemType, newItem: ItemType): Boolean {
        return oldItem == newItem
    }
}