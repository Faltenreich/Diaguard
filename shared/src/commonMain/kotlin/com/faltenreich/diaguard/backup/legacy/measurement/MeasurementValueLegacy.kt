package com.faltenreich.diaguard.backup.legacy.measurement

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.database.DatabaseKey

// TODO: Replace with MeasurementValue.Legacy
data class MeasurementValueLegacy(
    val createdAt: DateTime,
    val updatedAt: DateTime,
    val value: Double,
    val propertyKey: DatabaseKey.MeasurementProperty,
    val entryId: Long,
)