package com.faltenreich.diaguard.export.preference

import com.faltenreich.diaguard.data.export.PdfLayout
import com.faltenreich.diaguard.data.preference.Preference

data object PdfLayoutPreference : Preference<Int, PdfLayout> {

    override val key = "preference_export_pdf_layout"

    override val default = PdfLayout.TABLE

    override val onRead = { stableId: Int -> PdfLayout.entries.firstOrNull { it.stableId == stableId } }

    override val onWrite = PdfLayout::stableId
}