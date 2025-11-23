package com.psb.cashbuddy.data.db.models

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class TransactionDenomination @JvmOverloads constructor(
    @Id
    var id: Long = 0,
    @Convert(converter = DenominationTypeConverter::class, dbType = Int::class)
    val type: DenominationType = DenominationType.DEFAULT,
    val count: Int = 0,
) {
    lateinit var transaction: ToOne<TransactionHistory>
}