package com.aitgacem.budgeter.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.databinding.FragmentDetailsBinding
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.components.ItemType
import com.aitgacem.budgeter.ui.toFormattedDate
import com.aitgacem.budgeter.ui.components.toIcon

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
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
        binding.transactionDate.text = transaction.date.toString().toFormattedDate()
        binding.transactionDescription.text = transaction.title
        binding.transactionIcon.setImageDrawable(
            AppCompatResources.getDrawable(
                view.context,
                transaction.category.toIcon()
            )
        )

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
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
                    true
                }

                else -> {
                    false
                }
            }
        }
        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_transaction_details, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }

        })

    }
}