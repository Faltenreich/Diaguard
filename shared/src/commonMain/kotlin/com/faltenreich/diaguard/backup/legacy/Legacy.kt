package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.shared.datetime.DateTime

interface Legacy {

    val id: Long
    val createdAt: DateTime
    val updatedAt: DateTime
}