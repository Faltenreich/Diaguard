package com.faltenreich.diaguard.system.settings

class OpenNotificationSettingsUseCase(private val systemSettings: SystemSettings) {

    operator fun invoke() {
        systemSettings.openNotificationSettings()
    }
}