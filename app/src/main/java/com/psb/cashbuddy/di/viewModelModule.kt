package com.psb.cashbuddy.di

import com.psb.cashbuddy.data.db.OBUtils
import com.psb.cashbuddy.data.repository.CashRepository
import com.psb.cashbuddy.ui.fragments.creditDebit.CreditDebitViewModel
import com.psb.cashbuddy.ui.fragments.history.HistoryViewModel
import com.psb.cashbuddy.ui.fragments.summary.SummaryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // Dependency: view models
    viewModel {
        SummaryViewModel(get())
    }
    viewModel {
        HistoryViewModel(get())
    }
    viewModel {
        CreditDebitViewModel(get())
    }

    // Dependency: repositories
    single {
        CashRepository(get())
    }
    single {
        OBUtils(get())
    }
}