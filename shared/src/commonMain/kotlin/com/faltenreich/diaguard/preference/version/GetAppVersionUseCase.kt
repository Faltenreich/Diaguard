package com.faltenreich.diaguard.preference.version

import com.faltenreich.diaguard.shared.config.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetAppVersionUseCase(
    private val buildConfig: BuildConfig,
) {

    operator fun invoke(): Flow<String> {
        return flowOf("${buildConfig.getVersionName()} (${buildConfig.getVersionCode()})")
    }
}