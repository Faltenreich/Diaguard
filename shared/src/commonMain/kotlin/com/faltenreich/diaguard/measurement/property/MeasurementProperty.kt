package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing one medical property of the human body
 */
data class MeasurementProperty(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val name: String,
    val icon: String?,
    val sortIndex: Long,
) : DatabaseEntity {

    lateinit var types: List<MeasurementType>

    val isBloodSugar: Boolean
        get() = id == BLOOD_SUGAR_ID

    companion object {

        // TODO: Identify Blood Sugar accordingly
        const val BLOOD_SUGAR_ID = 1L
    }
}