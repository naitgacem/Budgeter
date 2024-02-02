package com.aitgacem.budgeter.ui.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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