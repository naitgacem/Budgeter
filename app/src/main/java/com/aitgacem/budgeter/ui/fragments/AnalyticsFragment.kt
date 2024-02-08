package com.aitgacem.budgeter.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aitgacem.budgeter.databinding.FragmentAnalyticsScreenBinding
import com.aitgacem.budgeter.ui.components.ItemType
import com.aitgacem.budgeter.ui.components.ItemType.*
import com.aitgacem.budgeter.ui.viewmodels.AnalyticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalyticsFragment : Fragment() {
    private lateinit var binding: FragmentAnalyticsScreenBinding
    private val viewModel: AnalyticsViewModel by viewModels()
    private var currMonth = mutableListOf<Pair<Long, Double>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalyticsScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dateAndBalance.observe(viewLifecycleOwner) {
            currMonth = it.filter { (date, _) ->
                date > 1706742000000L
            }.toList().toMutableList()
            binding.lineChart.init(currMonth)
            binding.lineChart.invalidate()
        }
    }
}