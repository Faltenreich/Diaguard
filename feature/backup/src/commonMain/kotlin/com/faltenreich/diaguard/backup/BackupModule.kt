package com.faltenreich.diaguard.backup

import com.faltenreich.diaguard.backup.read.ReadBackupFormViewModel
import com.faltenreich.diaguard.backup.write.WriteBackupFormViewModel
import com.faltenreich.diaguard.data.dataModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun backupModule() = module {
    includes(dataModule())
    viewModelOf(::WriteBackupFormViewModel)
    viewModelOf(::ReadBackupFormViewModel)
}