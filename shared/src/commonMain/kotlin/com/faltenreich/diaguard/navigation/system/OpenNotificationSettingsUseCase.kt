package com.faltenreich.diaguard.navigation.system

class OpenNotificationSettingsUseCase(private val systemSettings: SystemSettings) {

    operator fun invoke() {
        systemSettings.openNotificationSettings()
    }
}