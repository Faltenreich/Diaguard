package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.StringResource

enum class PdfLayout(label: StringResource) {

    TABLE(MR.strings.table),
    TIMELINE(MR.strings.timeline),
    LOG(MR.strings.log),
}