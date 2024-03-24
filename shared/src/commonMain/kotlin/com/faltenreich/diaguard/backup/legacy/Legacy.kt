package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.datetime.DateTime

interface Legacy {

    val createdAt: DateTime
    val updatedAt: DateTime
}