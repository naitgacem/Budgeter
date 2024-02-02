package com.aitgacem.budgeter.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.databinding.FragmentOverviewScreenBinding
import com.aitgacem.budgeter.ui.components.RecentTransactionsRecyclerViewAdapter
import com.aitgacem.budgeter.ui.viewmodels.OverviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : Fragment() {
    lateinit var binding: FragmentOverviewScreenBinding
    private val viewModel: OverviewViewModel by viewModels()

    @Inject
    lateinit var repository: TransactionsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transactions = mutableListOf<Transaction>()
        val rva = RecentTransactionsRecyclerViewAdapter(transactions)
        binding.recentTransactionsRv.layoutManager = LinearLayoutManager(this.activity)
        binding.recentTransactionsRv.adapter = rva

        lifecycleScope.launch {
            repository.readRecentTransactionsFromDatabase().collect {
                transactions.addAll(it)
                transactions.addAll(it)
                transactions.addAll(it)
                transactions.addAll(it)
                rva.notifyDataSetChanged()
            }
        }

    }
}