package com.faltenreich.diaguard.backup.legacy.measurement

import com.faltenreich.diaguard.backup.legacy.Legacy
import com.faltenreich.diaguard.shared.datetime.DateTime

data class EntryLegacy(
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val id: Long,
    val dateTime: DateTime,
    val note: String,
) : Legacy