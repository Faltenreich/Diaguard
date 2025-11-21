package com.faltenreich.diaguard.shared.system

import com.faltenreich.diaguard.system.settings.SystemSettings

class FakeSystemSettings : SystemSettings {

    override fun openNotificationSettings() = Unit
}