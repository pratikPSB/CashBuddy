package com.psb.cashbuddy.data.db.models

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

@Entity
data class Denomination @JvmOverloads constructor(
    @Id()
    var id: Long = 0,
    @Convert(converter = DenominationTypeConverter::class, dbType = Int::class)
    val type: DenominationType,
    var count: Int,
)

enum class DenominationType(val value: Int) {
    FIVE_HUNDRED(500), TWO_HUNDRED(200), ONE_HUNDRED(100), FIFTY(50), TWENTY(20), TEN(10), DEFAULT(0);
}

class DenominationTypeConverter : PropertyConverter<DenominationType?, Int?> {
    override fun convertToEntityProperty(databaseValue: Int?): DenominationType? {
        if (databaseValue == null) {
            return null
        }
        for (role in DenominationType.entries) {
            if (role.value == databaseValue) {
                return role
            }
        }
        return DenominationType.DEFAULT
    }

    override fun convertToDatabaseValue(entityProperty: DenominationType?): Int? {
        return entityProperty?.value
    }
}
