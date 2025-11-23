package com.psb.cashbuddy.data.db.models

import io.objectbox.annotation.*
import io.objectbox.converter.PropertyConverter
import io.objectbox.relation.ToMany

@Entity
data class TransactionHistory @JvmOverloads constructor(
    @Id
    var id: Long = 0,
    val amount: Double,
    @Convert(converter = TransactionTypeConverter::class, dbType = Int::class)
    val type: TransactionType,
    @Type(DatabaseType.DateNano)
    val date: Long = System.currentTimeMillis()
) {
    @Backlink(to = "transaction")
    lateinit var denominations: ToMany<TransactionDenomination>
}

enum class TransactionType(val id: Int) {
    CREDIT(0), DEBIT(1), DEFAULT(2);
}

class TransactionTypeConverter : PropertyConverter<TransactionType?, Int?> {
    override fun convertToEntityProperty(databaseValue: Int?): TransactionType? {
        if (databaseValue == null) {
            return null
        }
        for (role in TransactionType.entries) {
            if (role.id == databaseValue) {
                return role
            }
        }
        return TransactionType.DEFAULT
    }

    override fun convertToDatabaseValue(entityProperty: TransactionType?): Int? {
        return entityProperty?.id
    }
}
