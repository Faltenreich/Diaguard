package com.faltenreich.diaguard.preference.store

import com.faltenreich.diaguard.shared.config.BuildConfig
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.primitive.format
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetAppVersionUseCase(
    private val buildConfig: BuildConfig = inject(),
) {

    operator fun invoke(): Flow<String> {
        return flowOf(
            "%s-%d".format(
                buildConfig.getVersionName(),
                buildConfig.getBuildNumber(),
            )
        )
    }
}