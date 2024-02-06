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
import com.aitgacem.budgeter.databinding.FragmentOverviewScreenBinding
import com.aitgacem.budgeter.ui.components.RecentAdapter
import com.aitgacem.budgeter.ui.viewmodels.OverviewViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : Fragment() {
    private lateinit var binding: FragmentOverviewScreenBinding
    private val viewModel: OverviewViewModel by viewModels()

    @Inject
    lateinit var repository: TransactionsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 600
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 600
        }

        viewModel.balanceLiveData.observe(viewLifecycleOwner) {
            binding.balanceAmount.text = it?.toString() ?: "0"
        }
        val recyclerView = binding.recentTransactionsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        val listAdapter = RecentAdapter {
            Snackbar.make(view, "Hey worked, ${it.amount}", Snackbar.LENGTH_SHORT).show()
        }
        recyclerView.adapter = listAdapter
        viewModel.recentLiveData.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }

        binding.depositBtn.setOnClickListener {
            val action = OverviewFragmentDirections.depositAction(true)
            view.findNavController().navigate(action)
        }
        binding.withdrawBtn.setOnClickListener {
            val action = OverviewFragmentDirections.depositAction(false)
            view.findNavController().navigate(action)
        }
    }
}