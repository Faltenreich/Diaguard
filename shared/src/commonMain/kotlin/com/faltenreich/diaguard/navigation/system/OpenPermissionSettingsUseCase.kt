package com.faltenreich.diaguard.navigation.system

import com.faltenreich.diaguard.shared.system.SystemSettings

class OpenPermissionSettingsUseCase(private val systemSettings: SystemSettings) {

    operator fun invoke() {
        systemSettings.openPermissionSettings()
    }
}