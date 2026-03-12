package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.export.preference.ExportCategoryPreference
import com.faltenreich.diaguard.export.preference.ExportPropertyPreference
import com.faltenreich.diaguard.export.preference.ExportTypePreference
import com.faltenreich.diaguard.export.preference.IncludeCalendarWeekPreference
import com.faltenreich.diaguard.export.preference.IncludeDateOfExportPreference
import com.faltenreich.diaguard.export.preference.IncludeDaysWithoutEntriesPreference
import com.faltenreich.diaguard.export.preference.IncludeFoodEatenPreference
import com.faltenreich.diaguard.export.preference.IncludeNotesPreference
import com.faltenreich.diaguard.export.preference.IncludePageNumberPreference
import com.faltenreich.diaguard.export.preference.IncludeTagsPreference
import com.faltenreich.diaguard.export.preference.PdfLayoutPreference
import com.faltenreich.diaguard.preference.SetPreferenceUseCase

class SetExportSettingsUseCase(
    private val setPreference: SetPreferenceUseCase,
) {

    suspend operator fun invoke(settings: ExportSettings) {
        setPreference(ExportTypePreference, settings.exportType)
        setPreference(PdfLayoutPreference, settings.pdfLayout)

        setPreference(IncludeCalendarWeekPreference, settings.includeCalendarWeek)
        setPreference(IncludeDateOfExportPreference, settings.includeDateOfExport)
        setPreference(IncludeDaysWithoutEntriesPreference, settings.includeDaysWithoutEntries)
        setPreference(IncludePageNumberPreference, settings.includePageNumber)
        setPreference(IncludeNotesPreference, settings.includeNotes)
        setPreference(IncludeTagsPreference, settings.includeTags)
        setPreference(IncludeFoodEatenPreference, settings.includeFoodEaten)

        settings.categories.forEach { category ->
            setPreference(ExportCategoryPreference(category.category), category.isExported)
            category.properties.forEach { property ->
                setPreference(ExportPropertyPreference(property.property), property.isExported)
            }
        }
    }
}