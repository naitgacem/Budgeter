package com.aitgacem.budgeter.ui.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.data.model.Transaction

class RecentTransactionsRecyclerViewAdapter(private val dataSet: List<Transaction>) :
    RecyclerView.Adapter<RecentTransactionsRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val transactionTitle: TextView
        val transactionAmount: TextView
        val transactionIcon: ImageView

        init {
            transactionTitle = view.findViewById(R.id.transaction_title)
            transactionAmount = view.findViewById(R.id.transaction_amount)
            transactionIcon = view.findViewById(R.id.transaction_icon)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_recent_transaction, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.transactionTitle.text = dataSet[position].title
        viewHolder.transactionAmount.text = dataSet[position].amount.toString()
        val context = viewHolder.itemView.context
        viewHolder.transactionIcon.setImageDrawable(context.getDrawable(dataSet[position].category.toIcon()))
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}