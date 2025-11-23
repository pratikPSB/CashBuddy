package com.psb.cashbuddy.ui.fragments.creditDebit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psb.cashbuddy.data.db.models.DenominationType
import com.psb.cashbuddy.data.repository.CashRepository
import kotlinx.coroutines.launch

class CreditDebitViewModel(val repository: CashRepository) : ViewModel() {

    private val mutableErrorHandler = MutableLiveData<String>()
    val errorHandler: LiveData<String> = mutableErrorHandler

    fun createTransaction(list: List<String>) {
        viewModelScope.launch {
            if (list.all { it.isEmpty() }) {
                mutableErrorHandler.value = "Enter at least one denomination."
                return@launch
            }

            val map = DenominationType.entries.toTypedArray().mapIndexedNotNull { index, type ->
                val count = list.getOrNull(index)?.toIntOrNull()
                if (count != null && count > 0) {
                    type to count
                } else {
                    null
                }
            }.toMap()

            try {
                repository.credit(map)
            } catch (e: Exception) {
                mutableErrorHandler.value = e.message
            }
        }
    }

    fun debitTransaction(amount: String) {
        viewModelScope.launch {
            if (amount.isEmpty()) {
                mutableErrorHandler.value = "Enter amount."
                return@launch
            }
            try {
                repository.debit(amount.toInt())
            } catch (e: Exception) {
                mutableErrorHandler.value = e.message
            }
        }
    }
}