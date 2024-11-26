package com.faltenreich.diaguard.preference.version

import com.faltenreich.diaguard.preference.Preference
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.preference_version_code

data object VersionCodePreference : Preference<Int, Int> {

    override val key = Res.string.preference_version_code

    override val default = 0

    override val onRead = { versionCode: Int -> versionCode }

    override val onWrite = { versionCode: Int -> versionCode }
}