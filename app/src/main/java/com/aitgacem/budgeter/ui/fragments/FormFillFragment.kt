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
import com.aitgacem.budgeter.ui.components.ItemType
import com.aitgacem.budgeter.ui.components.toIcon
import com.aitgacem.budgeter.ui.getTimestamp
import com.aitgacem.budgeter.ui.viewmodels.FormFillViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FormFillFragment : Fragment() {

    private lateinit var binding: FragmentFormfillBinding
    private val viewModel: FormFillViewModel by viewModels()
    private var isDeposit: Boolean = false
    private val map = mutableMapOf<Int, Category>()
    private var isEdit: Boolean = false
    private var oldTransaction: ItemType.Transaction? = null

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
        oldTransaction = args.transaction
        if (oldTransaction != null) {
            isEdit = true
            preFill()
        }
        return binding.root
    }

    private fun preFill() {
        binding.transactionTitle.editText?.setText(oldTransaction?.title)
        binding.transactionAmount.editText?.setText(oldTransaction?.amount.toString())
        for ((k, v) in map) {
            if (oldTransaction?.category == v) {
                binding.transactionCategory.check(k)
            }
        }
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
                    map[chip.id] = it
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTransitions()

        binding.topbar.setOnClickListener { findNavController().popBackStack() }


        binding.saveBtn.setOnClickListener(::clickSave)
    }

    private fun clickSave(view: View) {
        val chipGroup = binding.transactionCategory
        with(viewModel) {
            updateDescription(binding.transactionTitle.editText?.text.toString())
            updateAmount(binding.transactionAmount.editText?.text.toString())
            val (day, month, year) = binding.transactionDate.let {
                Triple(it.dayOfMonth, it.month, it.year)
            }
            val timestamp = getTimestamp(day, month, year)
            updateId(timestamp)
            val checkedId = chipGroup.checkedChipId
            updateCategory(map[checkedId] ?: Category.Others)
            if (isEdit) {
                updateTransaction(isDeposit, oldTransaction as ItemType.Transaction)
            } else {
                saveTransaction(isDeposit)
            }
        }
        val action =
            if (isEdit) FormFillFragmentDirections.finishEdit() else FormFillFragmentDirections.finishFormSubmit()
        findNavController().navigate(action)
    }

    private fun setupTransitions() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 600
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 600
        }
    }
}

