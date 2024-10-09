package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.Navigation

class GetLatestScreenResultUseCase(private val navigation: Navigation) {

    suspend operator fun <T> invoke(key: String, default: T? = null): T? {
        return navigation.collectLatestScreenResult(key, default)
    }
}