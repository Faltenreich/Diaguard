package com.faltenreich.diaguard.backup.seed

sealed interface SeedKey {

    // TODO: Test uniqueness
    val key: String

}