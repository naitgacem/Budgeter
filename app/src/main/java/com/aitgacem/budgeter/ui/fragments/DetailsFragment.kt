package com.aitgacem.budgeter.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.databinding.FragmentDetailsBinding
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.components.ItemType
import com.aitgacem.budgeter.ui.components.toIcon
import com.aitgacem.budgeter.ui.toFormattedDate
import com.aitgacem.budgeter.ui.viewmodels.FormFillViewModel
import com.google.android.material.transition.MaterialSharedAxis

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    lateinit var transaction: ItemType.Transaction
    private val viewModel: FormFillViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args: DetailsFragmentArgs by navArgs()
        transaction = args.transaction
        viewModel.initialize(transaction)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTransitions()

        with(viewModel) {
            amount.observe(viewLifecycleOwner) {
                binding.transactionAmount.text = it.toString()
            }
            category.observe(viewLifecycleOwner) {
                it?.let {
                    binding.transactionIcon.setImageDrawable(
                        AppCompatResources.getDrawable(view.context, it.toIcon())
                    )
                }
            }
            date.observe(viewLifecycleOwner) {
                binding.transactionDate.text = it.toString().toFormattedDate()
            }
            description.observe(viewLifecycleOwner) {
                binding.transactionDescription.text = it
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        setupMenuBar()


    }

    private fun setupMenuBar() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit_btn -> {
                    val action = DetailsFragmentDirections.editTranaction(
                        transaction.category == Category.Deposit,
                        transaction
                    )
                    findNavController().navigate(action)
                    true
                }

                R.id.attach_btn -> {
                    true
                }

                R.id.delete_btn -> {
                    viewModel.deleteTransaction(transaction)
                    findNavController().popBackStack()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun setupTransitions() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 200
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 200
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 200
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 200
        }
    }
}