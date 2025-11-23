package com.psb.cashbuddy.ui.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.psb.cashbuddy.databinding.FragmentHistoryBinding
import org.koin.android.ext.android.inject

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val adapter = HistoryAdapter()

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private val viewModel: HistoryViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        initViews()
        registerObservers()
        return binding.root
    }

    fun initViews() {
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter
    }

    fun registerObservers() {
        viewModel.transactionLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) adapter.setTransactions(it)
        }
    }
}