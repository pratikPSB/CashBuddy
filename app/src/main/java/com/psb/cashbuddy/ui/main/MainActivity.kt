package com.psb.cashbuddy.ui.main

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.psb.cashbuddy.R
import com.psb.cashbuddy.databinding.ActivityMainBinding
import com.psb.cashbuddy.ui.fragments.creditDebit.CreditDebitFragment
import com.psb.cashbuddy.ui.fragments.history.HistoryFragment
import com.psb.cashbuddy.ui.fragments.summary.SummaryFragment

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val systemBarStyle = SystemBarStyle.light(
            ContextCompat.getColor(this, R.color.colorPrimary),
            ContextCompat.getColor(this, R.color.colorPrimary)
        )
        enableEdgeToEdge(
            statusBarStyle = systemBarStyle,
        )
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        setupBottomNavigation()
    }

    fun setupBottomNavigation() {
        setSupportActionBar(binding.toolbar)
        loadFragment(
            CreditDebitFragment.newInstance(),
            getString(R.string.title_credit_debit)
        )
        binding.bottomNavigation.selectedItemId = R.id.nav_credit_debit
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_summary -> {
                    loadFragment(SummaryFragment.newInstance(), getString(R.string.title_summary))
                    true
                }

                R.id.nav_history -> {
                    loadFragment(HistoryFragment.newInstance(), getString(R.string.title_history))
                    true
                }

                R.id.nav_credit_debit -> {
                    loadFragment(
                        CreditDebitFragment.newInstance(),
                        getString(R.string.title_credit_debit)
                    )
                    true
                }

                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        binding.toolbar.title = title
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.container.id, fragment)
        transaction.commit()
    }
}