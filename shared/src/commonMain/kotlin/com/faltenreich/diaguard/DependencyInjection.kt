package com.faltenreich.diaguard

import com.faltenreich.diaguard.dashboard.dashboardModule
import com.faltenreich.diaguard.entry.entryModule
import com.faltenreich.diaguard.log.logModule
import com.faltenreich.diaguard.measurement.measurementModule
import com.faltenreich.diaguard.navigation.navigationModule
import com.faltenreich.diaguard.onboarding.onboardingModule
import com.faltenreich.diaguard.preference.preferenceModule
import com.faltenreich.diaguard.shared.architecture.coroutineModule
import com.faltenreich.diaguard.shared.clipboard.clipboardModule
import com.faltenreich.diaguard.shared.config.configModule
import com.faltenreich.diaguard.shared.database.databaseModule
import com.faltenreich.diaguard.shared.database.sqldelight.sqlDelightModule
import com.faltenreich.diaguard.shared.datetime.dateTimeModule
import com.faltenreich.diaguard.shared.keyvalue.keyValueStoreModule
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
    configModule(),
    clipboardModule(),
    keyValueStoreModule(),
    sqlDelightModule(),
    databaseModule(),
    dateTimeModule(),

    onboardingModule(),
    navigationModule(),
    dashboardModule(),
    logModule(),
    timelineModule(),
    entryModule(),
    measurementModule(),
    preferenceModule(),
)