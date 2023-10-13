package com.faltenreich.diaguard.backup.seed

interface SeedReader {

    operator fun invoke(): String
}