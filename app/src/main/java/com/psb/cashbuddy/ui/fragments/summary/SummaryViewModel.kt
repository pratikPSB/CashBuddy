package com.psb.cashbuddy.ui.fragments.summary

import androidx.lifecycle.ViewModel
import com.psb.cashbuddy.data.repository.CashRepository

class SummaryViewModel(val repository: CashRepository) : ViewModel() {
    val denominationLiveData = repository.getDenominationLiveData()
    val totalBalance = repository.getTotalBalance()
}