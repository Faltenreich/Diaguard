package com.faltenreich.diaguard.shared.config

import com.faltenreich.diaguard.core.localization.format
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetAppVersionUseCase(
    private val buildConfig: BuildConfig,
) {

    operator fun invoke(): Flow<String> {
        return flowOf(
            "%s (%d)".format(
                buildConfig.getVersionName(),
                buildConfig.getVersionCode(),
            )
        )
    }
}