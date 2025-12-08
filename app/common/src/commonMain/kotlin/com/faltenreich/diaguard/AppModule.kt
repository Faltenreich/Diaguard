package com.faltenreich.diaguard

import com.faltenreich.diaguard.architecture.architectureModule
import com.faltenreich.diaguard.backup.backupModule
import com.faltenreich.diaguard.config.configModule
import com.faltenreich.diaguard.dashboard.dashboardModule
import com.faltenreich.diaguard.datetime.dateTimeModule
import com.faltenreich.diaguard.entry.entryModule
import com.faltenreich.diaguard.export.exportModule
import com.faltenreich.diaguard.food.foodModule
import com.faltenreich.diaguard.localization.localizationModule
import com.faltenreich.diaguard.log.logModule
import com.faltenreich.diaguard.logging.loggingModule
import com.faltenreich.diaguard.main.GetBottomAppBarStyleUseCase
import com.faltenreich.diaguard.main.GetNavigationEventUseCase
import com.faltenreich.diaguard.main.GetTopAppBarStyleUseCase
import com.faltenreich.diaguard.main.MainViewModel
import com.faltenreich.diaguard.measurement.measurementModule
import com.faltenreich.diaguard.navigation.navigationModule
import com.faltenreich.diaguard.network.networkModule
import com.faltenreich.diaguard.persistence.persistenceModule
import com.faltenreich.diaguard.preference.preferenceModule
import com.faltenreich.diaguard.serialization.serializationModule
import com.faltenreich.diaguard.startup.startupModule
import com.faltenreich.diaguard.statistic.statisticModule
import com.faltenreich.diaguard.system.systemModule
import com.faltenreich.diaguard.tag.tagModule
import com.faltenreich.diaguard.timeline.timelineModule
import com.faltenreich.diaguard.view.viewModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun appModule() = module {
    includes(
        coreModule(),
        featureModule(),
    )
    factoryOf(::GetTopAppBarStyleUseCase)
    factoryOf(::GetBottomAppBarStyleUseCase)
    factoryOf(::GetNavigationEventUseCase)
    viewModelOf(::MainViewModel)
    viewModelOf(::AppViewModel)
}

private fun coreModule() = module {
    includes(
        architectureModule(),
        configModule(),
        dateTimeModule(),
        localizationModule(),
        loggingModule(),
        networkModule(),
        persistenceModule(inMemory = false),
        serializationModule(),
        systemModule(),
        viewModule(),
    )
}

private fun featureModule() = module {
    includes(
        backupModule(),
        dashboardModule(),
        entryModule(),
        exportModule(),
        foodModule(),
        logModule(),
        measurementModule(),
        navigationModule(),
        preferenceModule(),
        startupModule(),
        statisticModule(),
        tagModule(),
        timelineModule(),
    )
}