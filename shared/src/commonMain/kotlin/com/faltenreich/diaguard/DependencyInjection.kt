package com.faltenreich.diaguard

import com.faltenreich.diaguard.entry.entryModule
import com.faltenreich.diaguard.shared.architecture.coroutineModule
import com.faltenreich.diaguard.shared.clipboard.clipboardModule
import com.faltenreich.diaguard.shared.database.databaseModule
import com.faltenreich.diaguard.shared.database.sqldelight.sqlDelightModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

object DependencyInjection {

    fun setup(declaration: KoinAppDeclaration = {}) {
        startKoin {
            declaration()
            modules(mainModules())
        }
    }
}

private fun mainModules() = listOf(
    coroutineModule(),
    clipboardModule(),
    sqlDelightModule(),
    databaseModule(),
    entryModule(),
)