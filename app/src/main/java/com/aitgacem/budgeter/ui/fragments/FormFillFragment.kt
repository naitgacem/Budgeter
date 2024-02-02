package com.aitgacem.budgeter.ui.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aitgacem.budgeter.databinding.FragmentFormfillBinding
import com.aitgacem.budgeter.ui.viewmodels.DepositViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class FormFillFragment : Fragment() {
    lateinit var binding: FragmentFormfillBinding
    private val viewModel: DepositViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormfillBinding.inflate(layoutInflater, container, false)
        val args: FormFillFragmentArgs by navArgs()
        if (args.isDeposit) {
            binding.transactionCategory.visibility = GONE
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topbar.setOnClickListener { findNavController().popBackStack() }
        binding.saveBtn.setOnClickListener {
            viewModel.updateDescription(binding.transactionTitle.editText?.text.toString())
            viewModel.updateAmount(binding.transactionAmount.editText?.text.toString())
            val (day, month, year) = binding.transactionDate.let {
                Triple(it.dayOfMonth, it.month, it.year)
            }
            val timestamp = getTimestamp(day, month, year)
            viewModel.updateDate(timestamp)
            viewModel.saveTransaction()
            findNavController().popBackStack()
        }
    }
}

private fun getTimestamp(day: Int, month: Int, year: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, day)
    calendar.set(Calendar.MONTH, month - 1)  // Calendar months are zero-based
    calendar.set(Calendar.YEAR, year)

    // Set the time to midnight (00:00:00)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}