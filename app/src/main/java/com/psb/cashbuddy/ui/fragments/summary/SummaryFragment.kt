package com.psb.cashbuddy.ui.fragments.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.psb.cashbuddy.databinding.FragmentSummaryBinding
import org.koin.android.ext.android.inject

class SummaryFragment : Fragment() {

    lateinit var binding: FragmentSummaryBinding
    val adapter = SummaryAdapter()

    companion object Companion {
        fun newInstance() = SummaryFragment()
    }

    private val viewModel: SummaryViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSummaryBinding.inflate(inflater, container, false)

        initViews()
        registerObservers()
        return binding.root
    }

    private fun initViews() {
        binding.rvDenominations.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDenominations.adapter = adapter

        binding.tvTotalBalance.text = "₹ ${viewModel.totalBalance}"
    }

    private fun registerObservers() {
        viewModel.denominationLiveData.observe(viewLifecycleOwner) {
            adapter.setDenominations(it)
        }
    }
}