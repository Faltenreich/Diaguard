package com.faltenreich.diaguard.backup.seed

interface Seed {

    val key: String?

    val isSeed: Boolean
        get() = key != null

    val isUserGenerated: Boolean
        get() = !isSeed
}