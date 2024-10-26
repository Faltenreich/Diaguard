package com.faltenreich.diaguard

import com.faltenreich.diaguard.backup.legacy.legacyModule
import com.faltenreich.diaguard.backup.seed.seedModule
import com.faltenreich.diaguard.dashboard.dashboardModule
import com.faltenreich.diaguard.datetime.dateTimeModule
import com.faltenreich.diaguard.entry.entryModule
import com.faltenreich.diaguard.export.exportModule
import com.faltenreich.diaguard.food.foodModule
import com.faltenreich.diaguard.log.logModule
import com.faltenreich.diaguard.main.mainModule
import com.faltenreich.diaguard.measurement.measurementModule
import com.faltenreich.diaguard.navigation.navigationModule
import com.faltenreich.diaguard.preference.preferenceModule
import com.faltenreich.diaguard.shared.architecture.coroutineModule
import com.faltenreich.diaguard.shared.clipboard.clipboardModule
import com.faltenreich.diaguard.shared.config.configModule
import com.faltenreich.diaguard.shared.database.databaseModule
import com.faltenreich.diaguard.shared.file.fileModule
import com.faltenreich.diaguard.shared.keyvalue.keyValueStoreModule
import com.faltenreich.diaguard.shared.localization.localizationModule
import com.faltenreich.diaguard.shared.logging.loggerModule
import com.faltenreich.diaguard.shared.networking.networkingModule
import com.faltenreich.diaguard.shared.primitive.primitiveModule
import com.faltenreich.diaguard.shared.serialization.serializationModule
import com.faltenreich.diaguard.shared.theme.themeModule
import com.faltenreich.diaguard.statistic.statisticModule
import com.faltenreich.diaguard.tag.tagModule
import com.faltenreich.diaguard.timeline.timelineModule

fun appModules() = listOf(
    // Common
    primitiveModule(),
    loggerModule(),
    coroutineModule(),
    configModule(),
    localizationModule(),
    clipboardModule(),
    serializationModule(),
    fileModule(),
    keyValueStoreModule(),
    databaseModule(),
    networkingModule(),
    dateTimeModule(),
    // Feature
    themeModule(),
    seedModule(),
    legacyModule(),
    mainModule(),
    exportModule(),
    navigationModule(),
    dashboardModule(),
    logModule(),
    timelineModule(),
    entryModule(),
    measurementModule(),
    foodModule(),
    tagModule(),
    statisticModule(),
    preferenceModule(),
)