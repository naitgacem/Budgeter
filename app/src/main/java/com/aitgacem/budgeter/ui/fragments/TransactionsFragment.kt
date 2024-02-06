package com.aitgacem.budgeter.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.databinding.FragmentTransactionsBinding
import com.aitgacem.budgeter.ui.components.DayDecorator
import com.aitgacem.budgeter.ui.components.ItemType
import com.aitgacem.budgeter.ui.components.HistoryRvAdapter
import com.aitgacem.budgeter.ui.viewmodels.TransactionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsBinding
    private val viewModel: TransactionsViewModel by viewModels()


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

        val listAdapter = HistoryRvAdapter {
            val action = TransactionsFragmentDirections.loadTransactionDetails(it)
            view.findNavController().navigate(action)
        }
        val recyclerView = binding.listTransactionsRv
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = listAdapter
        val dividerItemDecoration = DayDecorator()
        recyclerView.addItemDecoration(dividerItemDecoration)

        viewModel.transactions.observe(viewLifecycleOwner) { it ->
            val new = mutableListOf<ItemType>()
            for ((key, value) in it) {
                new.addAll(mutableListOf(key))
                new.addAll(value)
            }
            listAdapter.submitList(new)
        }
    }
}