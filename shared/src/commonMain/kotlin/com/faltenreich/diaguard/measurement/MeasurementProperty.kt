package com.faltenreich.diaguard.measurement

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
    val sortIndex: Int,
    val selectedUnit: MeasurementUnit,
) : DatabaseEntity