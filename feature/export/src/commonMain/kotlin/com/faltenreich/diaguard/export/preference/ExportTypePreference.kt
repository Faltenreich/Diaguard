package com.faltenreich.diaguard.export.preference

import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.data.preference.Preference

data object ExportTypePreference : Preference<Int, ExportType> {

    override val key = "preference_export_type"

    override val default = ExportType.PDF

    override val onRead = { stableId: Int -> ExportType.entries.firstOrNull { it.stableId == stableId } }

    override val onWrite = ExportType::stableId
}