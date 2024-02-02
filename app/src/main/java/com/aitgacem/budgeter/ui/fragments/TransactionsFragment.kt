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
import com.aitgacem.budgeter.databinding.FragmentTransactionsBinding
import com.aitgacem.budgeter.ui.components.ListTransactionsRecyclerViewAdapter
import com.aitgacem.budgeter.ui.viewmodels.TransactionDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TransactionsFragment : Fragment() {

    lateinit var binding: FragmentTransactionsBinding
    private val viewModel: TransactionDetailsViewModel by viewModels()

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
        binding = FragmentTransactionsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val transactions = mutableListOf<Transaction>()
        val rva = ListTransactionsRecyclerViewAdapter(transactions)
        binding.listTransactionsRv.layoutManager = LinearLayoutManager(this.activity)
        binding.listTransactionsRv.adapter = rva

        lifecycleScope.launch {
            repository.readAllTransactionsFromDatabase().collect {
                transactions.addAll(it)
                transactions.addAll(it)
                transactions.addAll(it)
                transactions.addAll(it)
                rva.notifyDataSetChanged()
            }
        }
    }
}