package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.shared.database.DatabaseKey

interface Seedable {

    // Unique if not null
    val key: DatabaseKey?

    val isSeed: Boolean
        get() = key != null

    val isUserGenerated: Boolean
        get() = !isSeed
}