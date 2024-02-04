package com.aitgacem.budgeter.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.databinding.FragmentDetailsBinding
import com.aitgacem.budgeter.ui.components.ItemType

class DetailsFragment : Fragment() {
    lateinit var binding: FragmentDetailsBinding
    lateinit var transaction: ItemType.Transaction
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args: DetailsFragmentArgs by navArgs()
        transaction = args.transaction
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.transactionAmount.text = transaction.amount.toString()
        binding.transactionDate.text = transaction.date.toString()
        binding.transactionDescription.text = transaction.title
        val menu = binding.toolbarMenu.menu
        MenuInflater(context).inflate(R.menu.menu_transaction_details, menu)


    }
}