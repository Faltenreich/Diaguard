package com.faltenreich.diaguard.shared.system

class FakeSystemSettings : SystemSettings {

    override fun openNotificationSettings() = Unit

    override fun openPermissionSettings() = Unit
}