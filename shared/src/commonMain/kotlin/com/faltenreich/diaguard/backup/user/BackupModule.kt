package com.faltenreich.diaguard.backup.user

import com.faltenreich.diaguard.backup.user.read.ReadBackupFormViewModel
import com.faltenreich.diaguard.backup.user.write.WriteBackupFormViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun backupModule() = module {
    viewModelOf(::WriteBackupFormViewModel)
    viewModelOf(::ReadBackupFormViewModel)
}