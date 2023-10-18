package com.faltenreich.diaguard.backup.seed

interface Seed <Crop> {

    fun harvest(): Crop
}