package com.psb.cashbuddy.ui.fragments.creditDebit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.psb.cashbuddy.databinding.FragmentCreditDebitBinding
import org.koin.android.ext.android.inject

class CreditDebitFragment : Fragment() {

    lateinit var binding: FragmentCreditDebitBinding

    companion object {
        fun newInstance() = CreditDebitFragment()
    }

    private val viewModel: CreditDebitViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreditDebitBinding.inflate(inflater, container, false)

        initUI()
        registerObservers()
        return binding.root
    }

    private fun registerObservers() {
        viewModel.errorHandler.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initUI() {
        binding.toggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    binding.btnCredit.id -> {
                        binding.creditGroup.visibility = View.VISIBLE
                        binding.debitGroup.visibility = View.GONE
                    }

                    binding.btnDebit.id -> {
                        binding.creditGroup.visibility = View.GONE
                        binding.debitGroup.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.btnSubmitCredit.setOnClickListener {
            val list = listOf(
                binding.et500.text.toString(),
                binding.et200.text.toString(),
                binding.et100.text.toString(),
                binding.et50.text.toString(),
                binding.et20.text.toString(),
                binding.et10.text.toString()
            )
            viewModel.createTransaction(list)
        }

        binding.btnSubmitDebit.setOnClickListener {
            viewModel.debitTransaction(binding.etAmount.text.toString())
        }
    }
}