package com.faltenreich.diaguard.data

import com.faltenreich.diaguard.data.legacy.EmptyLegacyDao
import com.faltenreich.diaguard.data.legacy.LegacyDao
import org.koin.dsl.module

actual fun dataPlatformModule() = module {
    single<LegacyDao> { EmptyLegacyDao() }
}