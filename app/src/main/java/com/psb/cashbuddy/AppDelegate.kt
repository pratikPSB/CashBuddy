package com.psb.cashbuddy

import android.app.Application
import com.psb.cashbuddy.data.db.OBUtils
import com.psb.cashbuddy.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration

@OptIn(KoinExperimentalAPI::class)
class AppDelegate : Application(), KoinStartup {

    override fun onKoinStartup() = koinConfiguration {
        androidContext(this@AppDelegate)
        modules(listOf(viewModelModule))
    }
}