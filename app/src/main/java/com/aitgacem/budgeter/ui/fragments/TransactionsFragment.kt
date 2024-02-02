package com.aitgacem.budgeter.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aitgacem.budgeter.data.TransactionsRepository
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.databinding.FragmentTransactionsBinding
import com.aitgacem.budgeter.ui.components.ListTransactionsRecyclerViewAdapter
import com.aitgacem.budgeter.ui.components.TransactionAdapter
import com.aitgacem.budgeter.ui.viewmodels.TransactionsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsBinding
    private val viewModel: TransactionsViewModel by viewModels()

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

        val listAdapter = TransactionAdapter {
            Snackbar.make(
                view,
                "hey worked ${it.amount}",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        val recyclerView = binding.listTransactionsRv
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = listAdapter

        viewModel.transactionsLiveData.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }

    }
}