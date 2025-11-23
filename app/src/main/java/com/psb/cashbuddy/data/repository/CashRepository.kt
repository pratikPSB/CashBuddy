package com.psb.cashbuddy.data.repository

import com.psb.cashbuddy.data.db.OBUtils
import com.psb.cashbuddy.data.db.models.*
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.equal

class CashRepository(private val obUtils: OBUtils) {

    private val transactionBox = obUtils.store.boxFor<TransactionHistory>()
    private val denominationBox = obUtils.store.boxFor<Denomination>()
    private val txnDenominationBox = obUtils.store.boxFor<TransactionDenomination>()

    init {
        seedDenominationsIfNeeded()
    }

    private fun seedDenominationsIfNeeded() {
        if (denominationBox.isEmpty) {
            obUtils.store.runInTx {
                DenominationType.entries
                    .filter { it.value > 0 }   // skip DEFAULT(0)
                    .forEach { type ->
                        denominationBox.put(
                            Denomination(
                                type = type,
                                count = 0
                            )
                        )
                    }
            }
        }
    }

    // ------------------------------------------------------------------------
    // CREDIT (Denomination-wise)
    // ------------------------------------------------------------------------
    fun credit(input: Map<DenominationType, Int>) {
        if (input.values.all { it == 0 }) {
            throw IllegalArgumentException("Enter at least one denomination.")
        }
        if (input.values.any { it < 0 }) {
            throw IllegalArgumentException("Invalid denomination count.")
        }

        val amount = input.entries.sumOf { (type, count) ->
            type.value * count
        }

        if (amount <= 0) throw IllegalArgumentException("Total amount must be positive.")

        obUtils.store.runInTx {
            // Update master denomination counts
            input.forEach { (type, count) ->
                if (count == 0) return@forEach

                val denom = getDenomination(type)
                denom.count += count
                denominationBox.put(denom)
            }

            // Create transaction
            val txn = TransactionHistory(
                amount = amount.toDouble(),
                type = TransactionType.CREDIT
            )
            transactionBox.put(txn)

            // Store breakdown
            input.forEach { (type, count) ->
                if (count == 0) return@forEach

                val txnDenomination = TransactionDenomination(
                    type = type,
                    count = count,
                )
                txnDenomination.transaction.targetId = txn.id
                txnDenominationBox.put(
                    txnDenomination
                )
            }
        }
    }


    // ------------------------------------------------------------------------
    // DEBIT (Amount-based)
    // ------------------------------------------------------------------------
    fun debit(amount: Int) {
        validateDebitAmount(amount)

        val breakdown = computeDebitDistribution(amount)

        obUtils.store.runInTx {

            // Deduct from master denominations
            breakdown.forEach { (type, count) ->
                val denom = getDenomination(type)
                denom.count -= count
                denominationBox.put(denom)
            }

            // Create transaction
            val txn = TransactionHistory(
                amount = amount.toDouble(),
                type = TransactionType.DEBIT
            )
            transactionBox.put(txn)

            breakdown.forEach { (type, count) ->
                val txnDenomination = TransactionDenomination(
                    type = type,
                    count = count,
                )
                txnDenomination.transaction.targetId = txn.id
                txnDenominationBox.put(
                    txnDenomination
                )
            }
        }
    }


    // ------------------------------------------------------------------------
    // Debit Helpers
    // ------------------------------------------------------------------------
    private fun validateDebitAmount(amount: Int): IllegalArgumentException? {
        return if (amount <= 0) IllegalArgumentException("Amount must be greater than zero.")
        else if (amount % 10 != 0) IllegalArgumentException("Amount must be multiple of ₹10.")
        else if (getTotalBalance() < amount) IllegalArgumentException("Insufficient balance.")
        else null
    }

    private fun computeDebitDistribution(amount: Int): Map<DenominationType, Int> {
        val result = mutableMapOf<DenominationType, Int>()
        var remaining = amount

        val all = denominationBox.all.associateBy { it.type }

        DenominationType.entries
            .filter { it.value > 0 }
            .sortedByDescending { it.value }
            .forEach { denom ->
                val available = all[denom]?.count ?: 0
                if (available == 0) return@forEach

                val needed = remaining / denom.value
                if (needed > 0) {
                    val used = minOf(needed, available)
                    result[denom] = used
                    remaining -= used * denom.value
                }
            }

        if (remaining != 0) {
            throw IllegalArgumentException("Cannot dispense exact amount with available denominations.")
        }

        return result
    }


    // ------------------------------------------------------------------------
    // Common Helpers
    // ------------------------------------------------------------------------
    private fun getDenomination(type: DenominationType): Denomination {
        return denominationBox.query()
            .equal(Denomination_.type, type.value)
            .build()
            .findFirst()
            ?: throw IllegalStateException("Denomination row missing: $type")
    }

    fun getTotalBalance(): Int {
        return denominationBox.all.sumOf { it.type.value * it.count }
    }

    fun getDenominationLiveData(): ObjectBoxLiveData<Denomination?> {
        return ObjectBoxLiveData(
            denominationBox.query().order(
                Denomination_.id
            ).build()
        )
    }

    fun getTransactionLiveData(): ObjectBoxLiveData<TransactionHistory?> {
        return ObjectBoxLiveData(
            transactionBox.query().order(
                TransactionHistory_.date
            ).build()
        )
    }
}
