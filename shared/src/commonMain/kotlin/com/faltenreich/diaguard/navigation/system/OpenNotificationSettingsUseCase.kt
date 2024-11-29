package com.faltenreich.diaguard.navigation.system

import com.faltenreich.diaguard.shared.system.SystemSettings

class OpenNotificationSettingsUseCase(private val systemSettings: SystemSettings) {

    operator fun invoke() {
        systemSettings.openNotificationSettings()
    }
}