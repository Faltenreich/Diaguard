package com.faltenreich.diaguard.data.preference.version

import com.faltenreich.diaguard.data.preference.Preference
import diaguard.data.generated.resources.Res
import diaguard.data.generated.resources.preference_version_code

data object VersionCodePreference : Preference<Long, Long> {

    override val key = Res.string.preference_version_code

    override val default = 0L

    override val onRead = { versionCode: Long -> versionCode }

    override val onWrite = { versionCode: Long -> versionCode }
}