package com.aitgacem.budgeter.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.databinding.FragmentOverviewScreenBinding
import com.aitgacem.budgeter.ui.components.RecentAdapter
import com.aitgacem.budgeter.ui.viewmodels.OverviewViewModel
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverviewFragment : Fragment() {
    private lateinit var binding: FragmentOverviewScreenBinding
    private val viewModel: OverviewViewModel by viewModels()

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
        setupTransitions()

        viewModel.balanceLiveData.observe(viewLifecycleOwner) {
            binding.balanceAmount.text = it?.toString() ?: "0"
        }

        setupRecyclerView()
        binding.topBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings_btn -> {
                    val action = OverviewFragmentDirections.gotoSettings()
                    findNavController().navigate(action)
                }
            }
            false
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

    private fun setupRecyclerView() {
        val recyclerView = binding.recentTransactionsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        val listAdapter = RecentAdapter {
            val action = OverviewFragmentDirections.loadTransactionDetails(it)
            findNavController().navigate(action)
        }
        recyclerView.adapter = listAdapter
        viewModel.recentLiveData.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
    }

    private fun setupTransitions() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 200
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 200
        }
    }
}