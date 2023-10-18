package com.faltenreich.diaguard.backup.seed

interface Seedable {

    // Unique if not null
    val key: String?

    val isSeed: Boolean
        get() = key != null

    val isUserGenerated: Boolean
        get() = !isSeed
}