package com.faltenreich.diaguard.shared.config

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetAppVersionUseCase(
    private val buildConfig: BuildConfig,
) {

    operator fun invoke(): Flow<String> {
        return flowOf("${buildConfig.getVersionName()} (${buildConfig.getVersionCode()})")
    }
}