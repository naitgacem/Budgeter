package com.aitgacem.budgeter.ui.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.data.model.Transaction

class ListTransactionsRecyclerViewAdapter(private val dataSet: List<Transaction>) :
    RecyclerView.Adapter<ListTransactionsRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val transactionTitle: TextView
        val transactionAmount: TextView
        val transactionTime: TextView
        val transactionIcon: ImageView

        init {
            transactionTitle = view.findViewById(R.id.transaction_title)
            transactionAmount = view.findViewById(R.id.transaction_amount)
            transactionIcon = view.findViewById(R.id.transaction_icon)
            transactionTime = view.findViewById(R.id.transaction_time)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_list_transactions, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.transactionTitle.text = dataSet[position].title
        viewHolder.transactionTime.text = "09:43"
        viewHolder.transactionAmount.text = dataSet[position].amount.toString()
        val context = viewHolder.itemView.context
        viewHolder.transactionIcon.setImageDrawable(context.getDrawable(dataSet[position].category.toIcon()))
    }

    override fun getItemCount() = dataSet.size

}


class TransactionAdapter(private val onClick: (Transaction) -> Unit) :
    ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback) {

    class TransactionViewHolder(itemView: View, private val onClick: (Transaction) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
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

        fun bind(transaction: Transaction) {
            currentTransaction = transaction
            transactionTitle.text = transaction.title
            transactionAmount.text = transaction.amount.toString()
            transactionTime.text = "09:42"
            val context = transactionTitle.context
            transactionIcon.setImageDrawable(context.getDrawable(transaction.category.toIcon()))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_transactions, parent, false)
        return TransactionViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val flower = getItem(position)
        holder.bind(flower)
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