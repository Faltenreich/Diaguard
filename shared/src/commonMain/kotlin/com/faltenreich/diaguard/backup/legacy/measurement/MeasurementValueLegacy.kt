package com.faltenreich.diaguard.backup.legacy.measurement

import com.faltenreich.diaguard.backup.legacy.Legacy
import com.faltenreich.diaguard.shared.datetime.DateTime

data class MeasurementValueLegacy(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val value: Double,
    val typeId: Long,
    val entryId: Long,
) : Legacy