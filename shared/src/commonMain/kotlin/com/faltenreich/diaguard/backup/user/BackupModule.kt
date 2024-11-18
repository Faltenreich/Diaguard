package com.faltenreich.diaguard.backup.user

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun backupModule() = module {
    viewModelOf(::BackupFormViewModel)
}