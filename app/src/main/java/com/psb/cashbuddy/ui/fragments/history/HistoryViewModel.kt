package com.psb.cashbuddy.ui.fragments.history

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.lifecycle.ViewModel
import com.psb.cashbuddy.data.db.models.TransactionHistory
import com.psb.cashbuddy.data.repository.CashRepository
import io.objectbox.Box
import io.objectbox.android.ObjectBoxLiveData


class HistoryViewModel(val repository: CashRepository) : ViewModel() {

    val transactionLiveData: ObjectBoxLiveData<TransactionHistory?> =
        repository.getTransactionLiveData()
}