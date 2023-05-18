package com.faltenreich.diaguard

import com.faltenreich.diaguard.dashboard.dashboardModule
import com.faltenreich.diaguard.entry.entryModule
import com.faltenreich.diaguard.log.logModule
import com.faltenreich.diaguard.measurement.measurementModule
import com.faltenreich.diaguard.shared.architecture.coroutineModule
import com.faltenreich.diaguard.shared.clipboard.clipboardModule
import com.faltenreich.diaguard.shared.database.databaseModule
import com.faltenreich.diaguard.shared.database.sqldelight.sqlDelightModule
import com.faltenreich.diaguard.shared.datetime.dateTimeModule
import com.faltenreich.diaguard.timeline.timelineModule
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
    dateTimeModule(),
    dashboardModule(),
    logModule(),
    timelineModule(),
    measurementModule(),
    entryModule(),
)