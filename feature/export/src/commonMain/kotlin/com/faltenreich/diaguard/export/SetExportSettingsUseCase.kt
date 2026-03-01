package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.export.preference.ExportTypePreference
import com.faltenreich.diaguard.export.preference.PdfLayoutPreference
import com.faltenreich.diaguard.preference.SetPreferenceUseCase

class SetExportSettingsUseCase(
    private val setPreference: SetPreferenceUseCase,
) {

    suspend operator fun invoke(settings: ExportSettings) {
        setPreference(ExportTypePreference, settings.exportType)
        setPreference(PdfLayoutPreference, settings.pdfLayout)
    }
}