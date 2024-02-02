package com.aitgacem.budgeter.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aitgacem.budgeter.R
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.balancelivedata.observe(viewLifecycleOwner) {
            binding.balanceAmount.text = it.toString()
        }


        binding.button.setOnClickListener {
            val action = OverviewFragmentDirections.depositAction(true)
            view.findNavController().navigate(action)
        }
        binding.button2.setOnClickListener {
            val action = OverviewFragmentDirections.depositAction(false)
            view.findNavController().navigate(action)
        }
    }
}