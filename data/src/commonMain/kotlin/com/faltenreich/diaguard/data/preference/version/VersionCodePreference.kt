package com.faltenreich.diaguard.data.preference.version

import com.faltenreich.diaguard.data.preference.Preference

data object VersionCodePreference : Preference<Long, Long> {

    override val key = "versionCode"

    override val default = 0L

    override val onRead = { versionCode: Long -> versionCode }

    override val onWrite = { versionCode: Long -> versionCode }
}