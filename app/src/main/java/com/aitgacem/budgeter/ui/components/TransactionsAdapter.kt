package com.aitgacem.budgeter.ui.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.data.model.Transaction

class TransactionAdapter(private val onClick: (Transaction) -> Unit) :
    ListAdapter<ItemType, ListViewHolder>(ListItemDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ListViewHolder.init(view, viewType, onClick)
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

sealed class ListViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    abstract fun bind(item: ItemType)

    companion object {
        fun init(view: View, viewType: Int, onClick: (Transaction) -> Unit): ListViewHolder {

            return when (viewType) {
                R.layout.item_day_divider -> HeaderViewHolder(view)
                R.layout.item_list_transactions -> TransactionViewHolder(view, onClick)
                else -> throw IllegalStateException()
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