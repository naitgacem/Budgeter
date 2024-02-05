package com.aitgacem.budgeter.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.databinding.FragmentFormfillBinding
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.components.toIcon
import com.aitgacem.budgeter.ui.viewmodels.FormFillViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class FormFillFragment : Fragment() {

    private lateinit var binding: FragmentFormfillBinding
    private val viewModel: FormFillViewModel by viewModels()
    private var isDeposit: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormfillBinding.inflate(layoutInflater, container, false)
        val args: FormFillFragmentArgs by navArgs()

        isDeposit = args.isDeposit
        val context = requireContext()

        setupSelectionChips(context, isDeposit)
        return binding.root
    }

    private fun setupSelectionChips(context: Context, isDeposit: Boolean) {
        if (isDeposit) {
            binding.transactionCategory.visibility = GONE
        } else {
            Category.entries.forEach {
                if (it != Category.Deposit) {
                    val chip = Chip(context)
                    val drawable =
                        ChipDrawable.createFromAttributes(context, null, 0, R.style.CustomChipStyle)
                    chip.setChipDrawable(drawable)
                    chip.text = it.name
                    chip.chipIcon = AppCompatResources.getDrawable(context, it.toIcon())
                    binding.transactionCategory.addView(chip)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 600
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 600
        }
        val chipGroup = binding.transactionCategory
        binding.topbar.setOnClickListener { findNavController().popBackStack() }
        binding.saveBtn.setOnClickListener {
            viewModel.updateDescription(binding.transactionTitle.editText?.text.toString())
            viewModel.updateAmount(binding.transactionAmount.editText?.text.toString())
            val (day, month, year) = binding.transactionDate.let {
                Triple(it.dayOfMonth, it.month, it.year)
            }
            val timestamp = getTimestamp(day, month, year)
            viewModel.updateId(timestamp)
            val chackedChip = chipGroup.checkedChipId
            print(chackedChip)
            viewModel.updateCategory(Category.Entertainment)
            viewModel.saveTransaction(isDeposit)
            findNavController().popBackStack()
        }
    }
}

private fun getTimestamp(day: Int, month: Int, year: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, day)
    calendar.set(Calendar.MONTH, month)  // Calendar months are zero-based
    calendar.set(Calendar.YEAR, year)

    // Set the time to midnight (00:00:00)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}